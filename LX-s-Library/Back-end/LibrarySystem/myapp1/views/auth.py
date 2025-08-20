from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework import status
from rest_framework_simplejwt.tokens import RefreshToken
import json
from ..models import User
from ..serializers import UserSerializer, UserCreateSerializer


@api_view(["POST"])
@permission_classes([AllowAny])
def user_register(request):
    """
    用户注册API
    """
    try:
        serializer = UserCreateSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()

            # 生成JWT令牌
            refresh = RefreshToken.for_user(user)

            return Response(
                {
                    "code": 1,
                    "message": "注册成功",
                    "user": UserSerializer(user).data,
                    "tokens": {
                        "access": str(refresh.access_token),
                        "refresh": str(refresh),
                    },
                },
                status=status.HTTP_201_CREATED,
            )
        else:
            return Response(
                {"message": "注册失败", "errors": serializer.errors},
                status=status.HTTP_400_BAD_REQUEST,
            )
    except Exception as e:
        return Response(
            {"message": "注册失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["POST"])
@permission_classes([AllowAny])
@csrf_exempt
def user_login(request):
    """
    用户登录API
    """
    try:
        username = request.data.get("username")
        password = request.data.get("password")

        if not username or not password:
            return Response(
                {"message": "用户名和密码不能为空"}, status=status.HTTP_400_BAD_REQUEST
            )

        # 验证用户凭据
        user = authenticate(username=username, password=password)

        if user is not None:
            # 登录用户
            login(request, user)

            # 生成JWT令牌
            refresh = RefreshToken.for_user(user)

            return Response(
                {
                    "code": 1,
                    "message": "登录成功",
                    "user": UserSerializer(user).data,
                    "token": str(refresh.access_token),
                },
                status=status.HTTP_200_OK,
            )
        else:
            return Response(
                {"code": 0, "message": "用户名或密码错误"},
                status=status.HTTP_200_OK,
            )
    except Exception as e:
        return Response(
            {"code": 0, "message": "登录失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["POST"])
def user_logout(request):
    """
    用户登出API
    """
    try:
        # 登出用户
        logout(request)

        return Response(
            {"code": 1, "message": "登出成功"}, status=status.HTTP_200_OK
        )
    except Exception as e:
        return Response(
            {"code": 0, "message": "登出失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["GET"])
def get_user_info(request):
    """
    获取当前用户信息API
    """
    try:
        if request.user.is_authenticated:
            return Response(
                {"code": 1, "user": UserSerializer(request.user).data},
                status=status.HTTP_200_OK,
            )
        else:
            return Response(
                {"code": 0, "message": "用户未登录"},
                status=status.HTTP_200_OK,
            )
    except Exception as e:
        return Response(
            {"code": 0, "message": "获取用户信息失败", "error": str(e)},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )


@api_view(["POST"])
@permission_classes([AllowAny])
def refresh_token(request):
    """
    刷新JWT令牌API
    """
    try:
        refresh_token = request.data.get("refresh")

        if not refresh_token:
            return Response(
                {"code": 0, "message": "刷新令牌不能为空"},
                status=status.HTTP_200_OK,
            )

        # 验证并刷新令牌
        refresh = RefreshToken(refresh_token)

        return Response(
            {
                "code": 1,
                "tokens": {
                    "access": str(refresh.access_token),
                    "refresh": str(refresh),
                }
            },
            status=status.HTTP_200_OK,
        )
    except Exception as e:
        return Response(
            {"code": 0, "message": "令牌刷新失败", "error": str(e)},
            status=status.HTTP_200_OK,
        )
