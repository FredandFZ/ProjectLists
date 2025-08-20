from django.db.models import Count, Q, Sum
from django.utils import timezone
from django.http import JsonResponse
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated, IsAdminUser
from rest_framework.response import Response
from rest_framework import status
from datetime import datetime, timedelta
from ..models import User, Book, Category, BorrowRecord


@api_view(['GET'])
@permission_classes([IsAuthenticated, IsAdminUser])
def dashboard_statistics(request):
    """
    获取仪表板统计数据
    包含用户、图书、借阅等基础统计信息
    """
    try:
        # 用户统计
        total_users = User.objects.count()
        active_users = User.objects.filter(is_active=True).count()
        staff_users = User.objects.filter(is_staff=True).count()
        
        # 图书统计
        total_books = Book.objects.count()
        available_books = Book.objects.filter(status='available').count()
        borrowed_books = Book.objects.filter(status='borrowed').count()
        
        # 分类统计
        total_categories = Category.objects.count()
        
        # 借阅记录统计
        total_borrow_records = BorrowRecord.objects.count()
        active_borrow_records = BorrowRecord.objects.filter(status='borrowed').count()
        returned_records = BorrowRecord.objects.filter(status='returned').count()
        overdue_records = BorrowRecord.objects.filter(status='overdue').count()
        
        # 今日统计
        today = timezone.now().date()
        today_borrows = BorrowRecord.objects.filter(
            borrow_date__date=today
        ).count()
        today_returns = BorrowRecord.objects.filter(
            return_date__date=today
        ).count()
        
        statistics = {
            'users': {
                'total': total_users,
                'active': active_users,
                'staff': staff_users,
            },
            'books': {
                'total': total_books,
                'available': available_books,
                'borrowed': borrowed_books,
            },
            'categories': {
                'total': total_categories,
            },
            'borrow_records': {
                'total': total_borrow_records,
                'active': active_borrow_records,
                'returned': returned_records,
                'overdue': overdue_records,
            },
            'today': {
                'borrows': today_borrows,
                'returns': today_returns,
            }
        }
        
        return Response({
            'success': True,
            'data': statistics
        })
        
    except Exception as e:
        return Response({
            'success': False,
            'error': str(e)
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


@api_view(['GET'])
@permission_classes([IsAuthenticated, IsAdminUser])
def book_statistics(request):
    """
    获取图书相关统计数据
    包含分类统计、热门图书等
    """
    try:
        # 按分类统计图书数量
        category_stats = Category.objects.annotate(
            book_count=Count('book')
        ).values('name', 'book_count').order_by('-book_count')
        
        # 热门图书（借阅次数最多的图书）
        popular_books = Book.objects.annotate(
            borrow_count=Count('borrowrecord')
        ).values('name', 'author', 'borrow_count').order_by('-borrow_count')[:10]
        
        # 图书状态分布
        book_status_stats = Book.objects.values('status').annotate(
            count=Count('id')
        )
        
        # 最近添加的图书
        recent_books = Book.objects.order_by('-created_at')[:5].values(
            'name', 'author', 'created_at'
        )
        
        statistics = {
            'category_distribution': list(category_stats),
            'popular_books': list(popular_books),
            'status_distribution': list(book_status_stats),
            'recent_books': list(recent_books),
        }
        
        return Response({
            'success': True,
            'data': statistics
        })
        
    except Exception as e:
        return Response({
            'success': False,
            'error': str(e)
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


@api_view(['GET'])
@permission_classes([IsAuthenticated, IsAdminUser])
def borrow_statistics(request):
    """
    获取借阅相关统计数据
    包含借阅趋势、逾期统计等
    """
    try:
        # 获取时间范围参数
        days = int(request.GET.get('days', 30))
        end_date = timezone.now().date()
        start_date = end_date - timedelta(days=days)
        
        # 每日借阅统计
        daily_borrows = []
        current_date = start_date
        while current_date <= end_date:
            count = BorrowRecord.objects.filter(
                borrow_date__date=current_date
            ).count()
            daily_borrows.append({
                'date': current_date.strftime('%Y-%m-%d'),
                'count': count
            })
            current_date += timedelta(days=1)
        
        # 每日归还统计
        daily_returns = []
        current_date = start_date
        while current_date <= end_date:
            count = BorrowRecord.objects.filter(
                return_date__date=current_date
            ).count()
            daily_returns.append({
                'date': current_date.strftime('%Y-%m-%d'),
                'count': count
            })
            current_date += timedelta(days=1)
        
        # 逾期统计
        overdue_stats = {
            'total_overdue': BorrowRecord.objects.filter(status='overdue').count(),
            'overdue_rate': 0,
        }
        
        total_active = BorrowRecord.objects.filter(status='borrowed').count()
        if total_active > 0:
            overdue_stats['overdue_rate'] = round(
                (overdue_stats['total_overdue'] / total_active) * 100, 2
            )
        
        # 借阅时长统计
        avg_borrow_duration = BorrowRecord.objects.filter(
            status='returned',
            return_date__isnull=False
        ).aggregate(
            avg_duration=Sum(
                timezone.now() - timezone.now()  # 这里需要计算实际的天数差
            )
        )
        
        statistics = {
            'daily_borrows': daily_borrows,
            'daily_returns': daily_returns,
            'overdue_stats': overdue_stats,
            'time_range': {
                'start_date': start_date.strftime('%Y-%m-%d'),
                'end_date': end_date.strftime('%Y-%m-%d'),
                'days': days
            }
        }
        
        return Response({
            'success': True,
            'data': statistics
        })
        
    except Exception as e:
        return Response({
            'success': False,
            'error': str(e)
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


@api_view(['GET'])
@permission_classes([IsAuthenticated, IsAdminUser])
def user_statistics(request):
    """
    获取用户相关统计数据
    包含活跃用户、借阅排行等
    """
    try:
        # 活跃用户排行（借阅次数最多的用户）
        active_users = User.objects.annotate(
            borrow_count=Count('borrowrecord')
        ).values('username', 'borrow_count').order_by('-borrow_count')[:10]
        
        # 用户注册趋势（最近30天）
        end_date = timezone.now().date()
        start_date = end_date - timedelta(days=30)
        
        daily_registrations = []
        current_date = start_date
        while current_date <= end_date:
            count = User.objects.filter(
                date_joined__date=current_date
            ).count()
            daily_registrations.append({
                'date': current_date.strftime('%Y-%m-%d'),
                'count': count
            })
            current_date += timedelta(days=1)
        
        # 用户类型分布
        user_type_stats = {
            'total': User.objects.count(),
            'active': User.objects.filter(is_active=True).count(),
            'staff': User.objects.filter(is_staff=True).count(),
            'inactive': User.objects.filter(is_active=False).count(),
        }
        
        # 最近注册的用户
        recent_users = User.objects.order_by('-date_joined')[:5].values(
            'username', 'email', 'date_joined', 'is_active'
        )
        
        statistics = {
            'active_users': list(active_users),
            'daily_registrations': daily_registrations,
            'user_type_distribution': user_type_stats,
            'recent_users': list(recent_users),
        }
        
        return Response({
            'success': True,
            'data': statistics
        })
        
    except Exception as e:
        return Response({
            'success': False,
            'error': str(e)
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


@api_view(['GET'])
@permission_classes([IsAuthenticated, IsAdminUser])
def comprehensive_statistics(request):
    """
    获取综合统计数据
    包含所有重要指标的汇总
    """
    try:
        # 基础统计
        total_users = User.objects.count()
        total_books = Book.objects.count()
        total_borrows = BorrowRecord.objects.count()
        
        # 今日统计
        today = timezone.now().date()
        today_borrows = BorrowRecord.objects.filter(
            borrow_date__date=today
        ).count()
        today_returns = BorrowRecord.objects.filter(
            return_date__date=today
        ).count()
        today_new_users = User.objects.filter(
            date_joined__date=today
        ).count()
        
        # 本月统计
        month_start = today.replace(day=1)
        month_borrows = BorrowRecord.objects.filter(
            borrow_date__date__gte=month_start
        ).count()
        month_returns = BorrowRecord.objects.filter(
            return_date__date__gte=month_start
        ).count()
        month_new_users = User.objects.filter(
            date_joined__date__gte=month_start
        ).count()
        
        # 逾期统计
        overdue_count = BorrowRecord.objects.filter(status='overdue').count()
        active_borrows = BorrowRecord.objects.filter(status='borrowed').count()
        overdue_rate = round((overdue_count / max(active_borrows, 1)) * 100, 2)
        
        # 图书利用率
        borrowed_books = Book.objects.filter(status='borrowed').count()
        book_utilization_rate = round((borrowed_books / max(total_books, 1)) * 100, 2)
        
        # 热门分类
        popular_categories = Category.objects.annotate(
            book_count=Count('book'),
            borrow_count=Count('book__borrowrecord')
        ).values('name', 'book_count', 'borrow_count').order_by('-borrow_count')[:5]
        
        comprehensive_stats = {
            'overview': {
                'total_users': total_users,
                'total_books': total_books,
                'total_borrows': total_borrows,
            },
            'today': {
                'borrows': today_borrows,
                'returns': today_returns,
                'new_users': today_new_users,
            },
            'this_month': {
                'borrows': month_borrows,
                'returns': month_returns,
                'new_users': month_new_users,
            },
            'rates': {
                'overdue_rate': overdue_rate,
                'book_utilization_rate': book_utilization_rate,
            },
            'popular_categories': list(popular_categories),
        }
        
        return Response({
            'success': True,
            'data': comprehensive_stats
        })
        
    except Exception as e:
        return Response({
            'success': False,
            'error': str(e)
        }, status=status.HTTP_500_INTERNAL_SERVER_ERROR) 