from .auth import (
    user_register,
    user_login,
    user_logout,
    get_user_info,
    refresh_token,
)

from .viewsets import (
    UserViewSet,
    CategoryViewSet,
    BookViewSet,
    BorrowRecordViewSet,
)

from .book_views import (
    borrow_book,
    return_book,
    get_user_borrow_records,
    get_overdue_records,
    search_books,
)

from .statistics import (
    dashboard_statistics,
    book_statistics,
    borrow_statistics,
    user_statistics,
    comprehensive_statistics,
)

__all__ = [
    # 认证相关
    "user_register",
    "user_login",
    "user_logout",
    "get_user_info",
    "refresh_token",
    # ViewSet
    "UserViewSet",
    "CategoryViewSet",
    "BookViewSet",
    "BorrowRecordViewSet",
    # 图书相关
    "borrow_book",
    "return_book",
    "get_user_borrow_records",
    "get_overdue_records",
    "search_books",
    # 统计相关
    "dashboard_statistics",
    "book_statistics",
    "borrow_statistics",
    "user_statistics",
    "comprehensive_statistics",
]
