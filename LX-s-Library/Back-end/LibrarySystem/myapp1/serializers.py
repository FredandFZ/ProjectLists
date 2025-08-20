from rest_framework import serializers
from .models import User, Category, Book, BorrowRecord


class UserSerializer(serializers.ModelSerializer):
    """用户序列化器"""

    class Meta:
        model = User
        fields = [
            "id",
            "username",
            "email",
            "first_name",
            "last_name",
            "phone",
            "is_staff",
            "created_at",
            "updated_at",
        ]
        read_only_fields = ["id", "created_at", "updated_at"]
        extra_kwargs = {"password": {"write_only": True}}


class UserCreateSerializer(serializers.ModelSerializer):
    """用户创建序列化器"""

    password = serializers.CharField(write_only=True)

    class Meta:
        model = User
        fields = ["username", "email", "password", "phone"]

    def create(self, validated_data):
        password = validated_data.pop("password")
        user = User(**validated_data)
        user.set_password(password)
        user.save()
        return user


class CategorySerializer(serializers.ModelSerializer):
    """图书分类序列化器"""

    class Meta:
        model = Category
        fields = ["id", "name", "created_at"]
        read_only_fields = ["id", "created_at"]


class BookSerializer(serializers.ModelSerializer):
    """图书序列化器"""

    category_name = serializers.CharField(source="category.name", read_only=True)
    status_display = serializers.CharField(source="get_status_display", read_only=True)

    class Meta:
        model = Book
        fields = [
            "id",
            "name",
            "author",
            "isbn",
            "category",
            "category_name",
            "publisher",
            "publish_date",
            "description",
            "status",
            "status_display",
            "created_at",
            "updated_at",
        ]
        read_only_fields = ["id", "created_at", "updated_at"]


class BookListSerializer(serializers.ModelSerializer):
    """图书列表序列化器（简化版）"""

    category_name = serializers.CharField(source="category.name", read_only=True)
    status_display = serializers.CharField(source="get_status_display", read_only=True)

    class Meta:
        model = Book
        fields = [
            "id",
            "name",
            "author",
            "isbn",
            "category_name",
            "publisher",
            "status",
            "status_display",
        ]


class BorrowRecordSerializer(serializers.ModelSerializer):
    """借阅记录序列化器"""

    user_username = serializers.CharField(source="user.username", read_only=True)
    book_name = serializers.CharField(source="book.name", read_only=True)
    status_display = serializers.CharField(source="get_status_display", read_only=True)
    is_overdue = serializers.BooleanField(read_only=True)

    class Meta:
        model = BorrowRecord
        fields = [
            "id",
            "user",
            "user_username",
            "book",
            "book_name",
            "borrow_date",
            "due_date",
            "return_date",
            "status",
            "status_display",
            "is_overdue",
            "created_at",
            "updated_at",
        ]
        read_only_fields = ["id", "borrow_date", "created_at", "updated_at"]


class BorrowRecordCreateSerializer(serializers.ModelSerializer):
    """借阅记录创建序列化器"""

    class Meta:
        model = BorrowRecord
        fields = ["user", "book", "due_date"]

    def validate(self, data):
        """验证借阅数据"""
        book = data["book"]
        user = data["user"]

        # 检查图书是否可借
        if book.status != "available":
            raise serializers.ValidationError("该图书当前不可借阅")

        # 检查用户是否有未归还的相同图书
        existing_borrow = BorrowRecord.objects.filter(
            user=user, book=book, status="borrowed"
        ).exists()

        if existing_borrow:
            raise serializers.ValidationError("您已借阅该图书，尚未归还")

        return data


class BorrowRecordReturnSerializer(serializers.ModelSerializer):
    """借阅记录归还序列化器"""

    class Meta:
        model = BorrowRecord
        fields = ["return_date"]

    def update(self, instance, validated_data):
        """更新归还日期并修改图书状态"""
        instance.return_date = validated_data.get("return_date")
        instance.status = "returned"
        instance.save()

        # 更新图书状态为可借阅
        book = instance.book
        book.status = "available"
        book.save()

        return instance
