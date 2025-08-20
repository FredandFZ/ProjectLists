from rest_framework import viewsets, permissions, filters
from rest_framework.decorators import action
from rest_framework.response import Response
from django_filters.rest_framework import DjangoFilterBackend
from ..models import User, Category, Book, BorrowRecord
from ..serializers import (
    UserSerializer,
    CategorySerializer,
    BookSerializer,
    BookListSerializer,
    BorrowRecordSerializer,
)


class UserViewSet(viewsets.ModelViewSet):
    """用户ViewSet"""

    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]
    filter_backends = [
        DjangoFilterBackend,
        filters.SearchFilter,
        filters.OrderingFilter,
    ]
    search_fields = ["username", "email", "phone"]
    ordering_fields = ["created_at", "username"]
    ordering = ["-created_at"]

    def get_permissions(self):
        """根据操作设置权限"""
        if self.action in ["create"]:
            return [permissions.AllowAny()]
        elif self.action in ["update", "partial_update", "destroy"]:
            return [permissions.IsAdminUser()]
        return super().get_permissions()

    @action(detail=False, methods=["get"])
    def me(self, request):
        """获取当前用户信息"""
        serializer = self.get_serializer(request.user)
        return Response(serializer.data)


class CategoryViewSet(viewsets.ModelViewSet):
    """图书分类ViewSet"""

    queryset = Category.objects.all()
    serializer_class = CategorySerializer
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]
    filter_backends = [
        DjangoFilterBackend,
        filters.SearchFilter,
        filters.OrderingFilter,
    ]
    search_fields = ["name"]
    ordering_fields = ["name", "created_at"]
    ordering = ["name"]

    def get_permissions(self):
        """根据操作设置权限"""
        if self.action in ["create", "update", "partial_update", "destroy"]:
            return [permissions.IsAdminUser()]
        return super().get_permissions()


class BookViewSet(viewsets.ModelViewSet):
    """图书ViewSet"""

    queryset = Book.objects.all()
    permission_classes = [permissions.IsAuthenticatedOrReadOnly]
    filter_backends = [
        DjangoFilterBackend,
        filters.SearchFilter,
        filters.OrderingFilter,
    ]
    filterset_fields = ["category", "status", "author"]
    search_fields = ["name", "author", "isbn", "publisher"]
    ordering_fields = ["name", "author", "created_at", "publish_date"]
    ordering = ["-created_at"]

    def get_serializer_class(self):
        """根据操作选择序列化器"""
        if self.action == "list":
            return BookListSerializer
        return BookSerializer

    def get_permissions(self):
        """根据操作设置权限"""
        if self.action in ["create", "update", "partial_update", "destroy"]:
            return [permissions.IsAdminUser()]
        return super().get_permissions()

    @action(detail=False, methods=["get"])
    def available(self, request):
        """获取可借阅的图书"""
        queryset = self.get_queryset().filter(status="available")
        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=["get"])
    def borrowed(self, request):
        """获取已借出的图书"""
        queryset = self.get_queryset().filter(status="borrowed")
        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)


class BorrowRecordViewSet(viewsets.ModelViewSet):
    """借阅记录ViewSet"""

    queryset = BorrowRecord.objects.all()
    serializer_class = BorrowRecordSerializer
    permission_classes = [permissions.IsAuthenticated]
    filter_backends = [
        DjangoFilterBackend,
        filters.SearchFilter,
        filters.OrderingFilter,
    ]
    filterset_fields = ["user", "book", "status"]
    search_fields = ["user__username", "book__name"]
    ordering_fields = ["borrow_date", "due_date", "return_date", "created_at"]
    ordering = ["-borrow_date"]

    def get_queryset(self):
        """根据用户权限过滤查询集"""
        if self.request.user.is_staff:
            return BorrowRecord.objects.all()
        return BorrowRecord.objects.filter(user=self.request.user)

    def get_permissions(self):
        """根据操作设置权限"""
        if self.action in ["create", "update", "partial_update", "destroy"]:
            return [permissions.IsAdminUser()]
        return super().get_permissions()

    @action(detail=False, methods=["get"])
    def my_records(self, request):
        """获取当前用户的借阅记录"""
        queryset = BorrowRecord.objects.filter(user=request.user)
        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=["get"])
    def overdue(self, request):
        """获取逾期记录"""
        if not request.user.is_staff:
            return Response({"message": "权限不足"}, status=403)

        from django.utils import timezone

        queryset = BorrowRecord.objects.filter(
            status="borrowed", due_date__lt=timezone.now()
        )
        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)
