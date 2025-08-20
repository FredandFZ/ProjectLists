from django.db import models
from django.contrib.auth.models import AbstractUser
from django.utils import timezone

# Create your models here.


class User(AbstractUser):
    """用户模型"""

    is_staff = models.BooleanField(default=False, verbose_name="是否为管理员")
    phone = models.CharField(
        max_length=11, blank=True, null=True, verbose_name="手机号"
    )
    created_at = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    updated_at = models.DateTimeField(auto_now=True, verbose_name="更新时间")

    class Meta:
        verbose_name = "用户"
        verbose_name_plural = "用户"

    def __str__(self):
        return self.username


class Category(models.Model):
    """图书分类模型"""

    name = models.CharField(max_length=50, unique=True, verbose_name="分类名称")
    created_at = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")

    class Meta:
        verbose_name = "图书分类"
        verbose_name_plural = "图书分类"

    def __str__(self):
        return self.name


class Book(models.Model):
    """图书模型"""

    STATUS_CHOICES = [
        ("available", "可借阅"),
        ("borrowed", "已借出"),
    ]

    name = models.CharField(max_length=200, verbose_name="书名")
    author = models.CharField(max_length=100, verbose_name="作者")
    isbn = models.CharField(max_length=13, unique=True, verbose_name="ISBN")
    category = models.ForeignKey(
        Category, on_delete=models.CASCADE, verbose_name="分类"
    )
    publisher = models.CharField(
        max_length=100, blank=True, null=True, verbose_name="出版社"
    )
    publish_date = models.DateField(blank=True, null=True, verbose_name="出版日期")
    description = models.TextField(blank=True, null=True, verbose_name="图书描述")
    status = models.CharField(
        max_length=20, choices=STATUS_CHOICES, default="available", verbose_name="状态"
    )
    created_at = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    updated_at = models.DateTimeField(auto_now=True, verbose_name="更新时间")

    class Meta:
        verbose_name = "图书"
        verbose_name_plural = "图书"

    def __str__(self):
        return self.name


class BorrowRecord(models.Model):
    """借阅记录模型"""

    STATUS_CHOICES = [
        ("borrowed", "已借出"),
        ("returned", "已归还"),
        ("overdue", "已逾期"),
    ]

    user = models.ForeignKey(User, on_delete=models.CASCADE, verbose_name="借阅用户")
    book = models.ForeignKey(Book, on_delete=models.CASCADE, verbose_name="借阅图书")
    borrow_date = models.DateTimeField(default=timezone.now, verbose_name="借阅日期")
    due_date = models.DateTimeField(verbose_name="应还日期")
    return_date = models.DateTimeField(
        blank=True, null=True, verbose_name="实际归还日期"
    )
    status = models.CharField(
        max_length=20, choices=STATUS_CHOICES, default="borrowed", verbose_name="状态"
    )
    created_at = models.DateTimeField(auto_now_add=True, verbose_name="创建时间")
    updated_at = models.DateTimeField(auto_now=True, verbose_name="更新时间")

    class Meta:
        verbose_name = "借阅记录"
        verbose_name_plural = "借阅记录"
        ordering = ["-borrow_date"]

    def __str__(self):
        return f"{self.user.username} - {self.book.name}"

    @property
    def is_overdue(self):
        """判断是否逾期"""
        if self.status == "borrowed" and timezone.now() > self.due_date:
            return True
        return False
