from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from django.utils import timezone
from django.shortcuts import get_object_or_404
from django.db.models import Q
from ..models import Book, BorrowRecord, User
from ..serializers import (
    BorrowRecordCreateSerializer,
    BorrowRecordReturnSerializer,
    BorrowRecordSerializer,
    BookSerializer,
)


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def borrow_book(request, book_id):
    """
    借阅图书API
    """
    try:
        book = get_object_or_404(Book, id=book_id)

        # 检查图书是否可借
        if book.status != "available":
            return Response(
                {"message": "该图书当前不可借阅"}, status=status.HTTP_400_BAD_REQUEST
            )

        # 检查用户是否已借阅该图书
        existing_borrow = BorrowRecord.objects.filter(
            user=request.user, book=book, status="borrowed"
        ).exists()

        if existing_borrow:
            return Response(
                {"message": "您已借阅该图书，尚未归还"},
                status=status.HTTP_400_BAD_REQUEST,
            )

        # 创建借阅记录
        borrow_data = {
            "user": request.user.id,
            "book": book.id,
            "due_date": request.data.get("due_date"),
        }

        serializer = BorrowRecordCreateSerializer(data=borrow_data)
        if serializer.is_valid():
            borrow_record = serializer.save()

            # 更新图书状态
            book.status = "borrowed"
            book.save()

            return Response(
                {
                    "message": "借阅成功",
                    "borrow_record": BorrowRecordSerializer(borrow_record).data,
                },
                status=status.HTTP_201_CREATED,
            )
        else:
            return Response(
                {"message": "借阅失败", "errors": serializer.errors},
                status=status.HTTP_400_BAD_REQUEST,
            )

    except Exception as e:
        return Response(
            {"message": "借阅失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def return_book(request, record_id):
    """
    归还图书API
    """
    try:
        borrow_record = get_object_or_404(BorrowRecord, id=record_id)

        # 检查权限（只能归还自己的借阅记录）
        if borrow_record.user != request.user and not request.user.is_staff:
            return Response({"message": "权限不足"}, status=status.HTTP_403_FORBIDDEN)

        # 检查是否已归还
        if borrow_record.status == "returned":
            return Response(
                {"message": "该图书已归还"}, status=status.HTTP_400_BAD_REQUEST
            )

        # 更新归还信息
        return_data = {"return_date": timezone.now()}

        serializer = BorrowRecordReturnSerializer(
            borrow_record, data=return_data, partial=True
        )

        if serializer.is_valid():
            updated_record = serializer.save()

            return Response(
                {
                    "message": "归还成功",
                    "borrow_record": BorrowRecordSerializer(updated_record).data,
                },
                status=status.HTTP_200_OK,
            )
        else:
            return Response(
                {"message": "归还失败", "errors": serializer.errors},
                status=status.HTTP_400_BAD_REQUEST,
            )

    except Exception as e:
        return Response(
            {"message": "归还失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["GET"])
@permission_classes([IsAuthenticated])
def get_user_borrow_records(request):
    """
    获取用户借阅记录API
    """
    try:
        # 获取当前用户的借阅记录
        borrow_records = BorrowRecord.objects.filter(user=request.user)

        # 支持分页
        page = request.query_params.get("page", 1)
        page_size = request.query_params.get("page_size", 20)

        try:
            page = int(page)
            page_size = int(page_size)
        except ValueError:
            page = 1
            page_size = 20

        # 计算分页
        start = (page - 1) * page_size
        end = start + page_size

        total_count = borrow_records.count()
        records = borrow_records[start:end]

        serializer = BorrowRecordSerializer(records, many=True)

        return Response(
            {
                "count": total_count,
                "next": (
                    f"?page={page + 1}&page_size={page_size}"
                    if end < total_count
                    else None
                ),
                "previous": (
                    f"?page={page - 1}&page_size={page_size}" if page > 1 else None
                ),
                "results": serializer.data,
            },
            status=status.HTTP_200_OK,
        )

    except Exception as e:
        return Response(
            {"message": "获取借阅记录失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["GET"])
@permission_classes([IsAuthenticated])
def get_overdue_records(request):
    """
    获取逾期记录API（仅管理员）
    """
    try:
        if not request.user.is_staff:
            return Response({"message": "权限不足"}, status=status.HTTP_403_FORBIDDEN)

        # 获取逾期记录
        overdue_records = BorrowRecord.objects.filter(
            status="borrowed", due_date__lt=timezone.now()
        )

        # 支持分页
        page = request.query_params.get("page", 1)
        page_size = request.query_params.get("page_size", 20)

        try:
            page = int(page)
            page_size = int(page_size)
        except ValueError:
            page = 1
            page_size = 20

        # 计算分页
        start = (page - 1) * page_size
        end = start + page_size

        total_count = overdue_records.count()
        records = overdue_records[start:end]

        serializer = BorrowRecordSerializer(records, many=True)

        return Response(
            {
                "count": total_count,
                "next": (
                    f"?page={page + 1}&page_size={page_size}"
                    if end < total_count
                    else None
                ),
                "previous": (
                    f"?page={page - 1}&page_size={page_size}" if page > 1 else None
                ),
                "results": serializer.data,
            },
            status=status.HTTP_200_OK,
        )

    except Exception as e:
        return Response(
            {"message": "获取逾期记录失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["GET"])
def search_books(request):
    """
    搜索图书API
    """
    try:
        # 获取搜索参数
        query = request.query_params.get("q", "")
        category = request.query_params.get("category", "")
        status = request.query_params.get("status", "")

        # 构建查询
        books = Book.objects.all()

        if query:
            books = books.filter(
                Q(name__icontains=query)
                | Q(author__icontains=query)
                | Q(isbn__icontains=query)
                | Q(publisher__icontains=query)
            )

        if category:
            books = books.filter(category_id=category)

        if status:
            books = books.filter(status=status)

        # 排序
        ordering = request.query_params.get("ordering", "-created_at")
        books = books.order_by(ordering)

        # 分页
        page = request.query_params.get("page", 1)
        page_size = request.query_params.get("page_size", 20)

        try:
            page = int(page)
            page_size = int(page_size)
        except ValueError:
            page = 1
            page_size = 20

        # 计算分页
        start = (page - 1) * page_size
        end = start + page_size

        total_count = books.count()
        book_list = books[start:end]

        serializer = BookSerializer(book_list, many=True)

        return Response(
            {
                "count": total_count,
                "next": (
                    f"?q={query}&category={category}&status={status}&ordering={ordering}&page={page + 1}&page_size={page_size}"
                    if end < total_count
                    else None
                ),
                "previous": (
                    f"?q={query}&category={category}&status={status}&ordering={ordering}&page={page - 1}&page_size={page_size}"
                    if page > 1
                    else None
                ),
                "results": serializer.data,
            },
            status=status.HTTP_200_OK,
        )

    except Exception as e:
        return Response(
            {"message": "搜索失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )
