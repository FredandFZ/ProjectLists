#!/usr/bin/env python
"""
图书馆管理系统快速启动脚本
"""

import os
import sys
import subprocess
from pathlib import Path


def check_dependencies():
    """检查依赖是否安装"""
    try:
        import django
        import rest_framework
        import rest_framework_simplejwt
        import django_filters

        print("✅ 所有依赖已安装")
        return True
    except ImportError as e:
        print(f"❌ 缺少依赖: {e}")
        print("请运行: pip install -r requirements.txt")
        return False


def check_database():
    """检查数据库连接"""
    try:
        import django

        os.environ.setdefault("DJANGO_SETTINGS_MODULE", "LibrarySystem.settings")
        django.setup()

        from django.db import connection

        with connection.cursor() as cursor:
            cursor.execute("SELECT 1")
        print("✅ 数据库连接正常")
        return True
    except Exception as e:
        print(f"❌ 数据库连接失败: {e}")
        print("请检查数据库配置和连接")
        return False


def run_migrations():
    """运行数据库迁移"""
    try:
        print("正在运行数据库迁移...")
        subprocess.run([sys.executable, "manage.py", "makemigrations"], check=True)
        subprocess.run([sys.executable, "manage.py", "migrate"], check=True)
        print("✅ 数据库迁移完成")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ 数据库迁移失败: {e}")
        return False


def create_superuser():
    """创建超级用户"""
    try:
        print("正在创建超级用户...")
        # 使用环境变量设置默认值
        os.environ.setdefault("DJANGO_SUPERUSER_USERNAME", "admin")
        os.environ.setdefault("DJANGO_SUPERUSER_EMAIL", "admin@example.com")
        os.environ.setdefault("DJANGO_SUPERUSER_PASSWORD", "admin123")

        subprocess.run(
            [sys.executable, "manage.py", "createsuperuser", "--noinput"], check=True
        )
        print("✅ 超级用户创建完成")
        print("用户名: admin")
        print("密码: admin123")
        return True
    except subprocess.CalledProcessError:
        print("⚠️ 超级用户可能已存在，跳过创建")
        return True


def generate_sample_data():
    """生成模拟数据"""
    try:
        print("正在生成模拟数据...")
        subprocess.run([sys.executable, "create_sample_data.py"], check=True)
        print("✅ 模拟数据生成完成")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ 模拟数据生成失败: {e}")
        return False


def start_server():
    """启动开发服务器"""
    try:
        print("正在启动开发服务器...")
        print("服务器地址: http://localhost:8080")
        print("管理后台: http://localhost:8080/admin")
        print("API文档: http://localhost:8080/api/")
        print("按 Ctrl+C 停止服务器")
        print("-" * 50)

        subprocess.run([sys.executable, "manage.py", "runserver", "8080"])
    except KeyboardInterrupt:
        print("\n服务器已停止")
    except Exception as e:
        print(f"❌ 启动服务器失败: {e}")


def show_menu():
    """显示菜单"""
    print("\n请选择操作:")
    print("1. 启动服务器")
    print("2. 生成模拟数据")
    print("3. 完整初始化（迁移 + 创建超级用户 + 生成数据 + 启动服务器）")
    print("4. 退出")
    
    while True:
        choice = input("\n请输入选项 (1-4): ").strip()
        if choice in ['1', '2', '3', '4']:
            return choice
        else:
            print("❌ 无效选项，请输入 1-4")


def main():
    """主函数"""
    print("图书馆管理系统启动脚本")
    print("=" * 50)

    # 检查当前目录
    if not Path("manage.py").exists():
        print("❌ 请在项目根目录运行此脚本")
        return

    # 检查依赖
    if not check_dependencies():
        return

    # 检查数据库
    if not check_database():
        return

    # 显示菜单
    choice = show_menu()
    
    if choice == '1':
        # 只启动服务器
        start_server()
    elif choice == '2':
        # 只生成模拟数据
        generate_sample_data()
    elif choice == '3':
        # 完整初始化
        print("\n开始完整初始化...")
        
        # 运行迁移
        if not run_migrations():
            return
        
        # 创建超级用户
        create_superuser()
        
        # 生成模拟数据
        if not generate_sample_data():
            return
        
        # 启动服务器
        start_server()
    elif choice == '4':
        print("再见！")
        return


if __name__ == "__main__":
    main()
