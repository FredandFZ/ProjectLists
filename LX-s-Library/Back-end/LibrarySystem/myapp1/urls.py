from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import (
    UserViewSet,
    CategoryViewSet,
    BookViewSet,
    BorrowRecordViewSet,
    user_register,
    user_login,
    user_logout,
    get_user_info,
    refresh_token,
    borrow_book,
    return_book,
    get_user_borrow_records,
    get_overdue_records,
    search_books,
    dashboard_statistics,
    book_statistics,
    borrow_statistics,
    user_statistics,
    comprehensive_statistics,
)

# 创建路由器
router = DefaultRouter(trailing_slash=False)
router.register(r"readers", UserViewSet)
router.register(r"categories", CategoryViewSet)
router.register(r"books", BookViewSet)
router.register(r"borrow-records", BorrowRecordViewSet)

urlpatterns = [
    # 包含路由器生成的URL
    path("api/", include(router.urls)),
    # 认证相关API
    path("api/auth/register", user_register, name="user_register"),
    path("api/auth/login", user_login, name="user_login"),
    path("api/auth/logout", user_logout, name="user_logout"),
    path("api/auth/user", get_user_info, name="get_user_info"),
    path("api/auth/refresh", refresh_token, name="refresh_token"),
    # 借阅相关API
    path("api/books/<int:book_id>/borrow", borrow_book, name="borrow_book"),
    path("api/borrow-records/<int:record_id>/return", return_book, name="return_book"),
    path(
        "api/user/borrow-records", get_user_borrow_records, name="user_borrow_records"
    ),
    path("api/overdue-records", get_overdue_records, name="overdue_records"),
    # 搜索API
    # path("api/books/search", search_books, name="search_books"),
    # 统计相关API
    path("api/statistics/dashboard", dashboard_statistics, name="dashboard_statistics"),
    path("api/statistics/books", book_statistics, name="book_statistics"),
    path("api/statistics/borrows", borrow_statistics, name="borrow_statistics"),
    path("api/statistics/readers", user_statistics, name="user_statistics"),
    path("api/statistics/comprehensive", comprehensive_statistics, name="comprehensive_statistics"),
]
