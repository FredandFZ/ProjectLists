#!/usr/bin/env python
"""
图书馆管理系统模拟数据生成脚本
"""

import os
import sys
import random
from datetime import datetime, timedelta
from django.utils import timezone

# 设置Django环境
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "LibrarySystem.settings")
import django
django.setup()

from myapp1.models import User, Category, Book, BorrowRecord


def create_categories():
    """创建图书分类"""
    categories_data = [
        "计算机科学",
        "文学小说",
        "历史传记",
        "科学技术",
        "经济管理",
        "艺术设计",
        "哲学宗教",
        "教育考试",
        "医学健康",
        "旅游地理"
    ]
    
    categories = []
    for name in categories_data:
        category, created = Category.objects.get_or_create(name=name)
        if created:
            print(f"✅ 创建分类: {name}")
        categories.append(category)
    
    return categories


def create_users():
    """创建普通用户"""
    users_data = [
        {"username": "zhang_san", "first_name": "张", "last_name": "三", "email": "zhangsan@example.com", "phone": "13800138001"},
        {"username": "li_si", "first_name": "李", "last_name": "四", "email": "lisi@example.com", "phone": "13800138002"},
        {"username": "wang_wu", "first_name": "王", "last_name": "五", "email": "wangwu@example.com", "phone": "13800138003"},
        {"username": "zhao_liu", "first_name": "赵", "last_name": "六", "email": "zhaoliu@example.com", "phone": "13800138004"},
        {"username": "qian_qi", "first_name": "钱", "last_name": "七", "email": "qianqi@example.com", "phone": "13800138005"},
        {"username": "sun_ba", "first_name": "孙", "last_name": "八", "email": "sunba@example.com", "phone": "13800138006"},
        {"username": "zhou_jiu", "first_name": "周", "last_name": "九", "email": "zhoujiu@example.com", "phone": "13800138007"},
        {"username": "wu_shi", "first_name": "吴", "last_name": "十", "email": "wushi@example.com", "phone": "13800138008"},
    ]
    
    users = []
    for user_data in users_data:
        user, created = User.objects.get_or_create(
            username=user_data["username"],
            defaults={
                "first_name": user_data["first_name"],
                "last_name": user_data["last_name"],
                "email": user_data["email"],
                "phone": user_data["phone"],
                "password": "password123"  # 简单密码，实际使用中应该加密
            }
        )
        if created:
            user.set_password("password123")
            user.save()
            print(f"✅ 创建用户: {user_data['username']}")
        users.append(user)
    
    return users


def create_books(categories):
    """创建图书"""
    books_data = [
        # 计算机科学
        {"name": "Python编程从入门到实践", "author": "埃里克·马瑟斯", "isbn": "9787115428028", "category": "计算机科学", "publisher": "人民邮电出版社", "description": "Python编程经典教程"},
        {"name": "算法导论", "author": "托马斯·H·科尔曼", "isbn": "9787111187776", "category": "计算机科学", "publisher": "机械工业出版社", "description": "计算机算法的经典教材"},
        {"name": "深入理解计算机系统", "author": "兰德尔·E·布莱恩特", "isbn": "9787111321330", "category": "计算机科学", "publisher": "机械工业出版社", "description": "计算机系统底层原理"},
        
        # 文学小说
        {"name": "百年孤独", "author": "加西亚·马尔克斯", "isbn": "9787544253994", "category": "文学小说", "publisher": "南海出版公司", "description": "魔幻现实主义文学经典"},
        {"name": "活着", "author": "余华", "isbn": "9787506365437", "category": "文学小说", "publisher": "作家出版社", "description": "中国当代文学经典作品"},
        {"name": "三体", "author": "刘慈欣", "isbn": "9787536692930", "category": "文学小说", "publisher": "重庆出版社", "description": "中国科幻文学代表作"},
        
        # 历史传记
        {"name": "史记", "author": "司马迁", "isbn": "9787101003048", "category": "历史传记", "publisher": "中华书局", "description": "中国第一部纪传体通史"},
        {"name": "明朝那些事儿", "author": "当年明月", "isbn": "9787801656087", "category": "历史传记", "publisher": "中国海关出版社", "description": "明朝历史的通俗讲述"},
        
        # 科学技术
        {"name": "时间简史", "author": "史蒂芬·霍金", "isbn": "9787535732309", "category": "科学技术", "publisher": "湖南科技出版社", "description": "宇宙物理学的科普经典"},
        {"name": "人类简史", "author": "尤瓦尔·赫拉利", "isbn": "9787508640757", "category": "科学技术", "publisher": "中信出版社", "description": "人类发展史的独特视角"},
        
        # 经济管理
        {"name": "经济学原理", "author": "曼昆", "isbn": "9787300078281", "category": "经济管理", "publisher": "北京大学出版社", "description": "经济学入门经典教材"},
        {"name": "管理学", "author": "彼得·德鲁克", "isbn": "9787111075752", "category": "经济管理", "publisher": "机械工业出版社", "description": "管理学经典著作"},
        
        # 艺术设计
        {"name": "艺术的故事", "author": "贡布里希", "isbn": "9787807463584", "category": "艺术设计", "publisher": "广西美术出版社", "description": "艺术史的经典著作"},
        {"name": "设计心理学", "author": "唐纳德·诺曼", "isbn": "9787115416940", "category": "艺术设计", "publisher": "中信出版社", "description": "设计思维的重要参考"},
        
        # 哲学宗教
        {"name": "苏菲的世界", "author": "乔斯坦·贾德", "isbn": "9787506394864", "category": "哲学宗教", "publisher": "作家出版社", "description": "哲学入门经典"},
        {"name": "论语", "author": "孔子", "isbn": "9787101003239", "category": "哲学宗教", "publisher": "中华书局", "description": "儒家经典著作"},
        
        # 教育考试
        {"name": "如何阅读一本书", "author": "莫提默·J·艾德勒", "isbn": "9787108015444", "category": "教育考试", "publisher": "商务印书馆", "description": "阅读方法的经典指南"},
        {"name": "学习之道", "author": "乔希·维茨金", "isbn": "9787115471659", "category": "教育考试", "publisher": "人民邮电出版社", "description": "高效学习的实用方法"},
        
        # 医学健康
        {"name": "黄帝内经", "author": "佚名", "isbn": "9787101003238", "category": "医学健康", "publisher": "中华书局", "description": "中医理论经典"},
        {"name": "营养圣经", "author": "帕特里克·霍尔福德", "isbn": "9787508640758", "category": "医学健康", "publisher": "中信出版社", "description": "营养学实用指南"},
        
        # 旅游地理
        {"name": "中国国家地理", "author": "中国国家地理杂志社", "isbn": "9787508640759", "category": "旅游地理", "publisher": "中国国家地理杂志社", "description": "中国地理文化百科全书"},
        {"name": "孤独星球", "author": "托尼·惠勒", "isbn": "9787508640760", "category": "旅游地理", "publisher": "中国地图出版社", "description": "全球旅行指南经典"},
    ]
    
    books = []
    for book_data in books_data:
        category = next(cat for cat in categories if cat.name == book_data["category"])
        
        # 随机生成出版日期（最近10年内）
        days_ago = random.randint(0, 3650)
        publish_date = datetime.now().date() - timedelta(days=days_ago)
        
        book, created = Book.objects.get_or_create(
            isbn=book_data["isbn"],
            defaults={
                "name": book_data["name"],
                "author": book_data["author"],
                "category": category,
                "publisher": book_data["publisher"],
                "publish_date": publish_date,
                "description": book_data["description"],
                "status": random.choice(["available", "borrowed"])
            }
        )
        if created:
            print(f"✅ 创建图书: {book_data['name']}")
        books.append(book)
    
    return books


def create_borrow_records(users, books):
    """创建借阅记录"""
    # 获取可借阅的图书
    available_books = [book for book in books if book.status == "available"]
    
    # 为每个用户创建一些借阅记录
    for user in users:
        # 随机选择1-3本书进行借阅
        num_books = random.randint(1, 3)
        selected_books = random.sample(available_books, min(num_books, len(available_books)))
        
        for book in selected_books:
            # 随机生成借阅日期（最近30天内）
            borrow_days_ago = random.randint(0, 30)
            borrow_date = timezone.now() - timedelta(days=borrow_days_ago)
            
            # 借阅期限为30天
            due_date = borrow_date + timedelta(days=30)
            
            # 随机决定是否已归还
            is_returned = random.choice([True, False])
            return_date = None
            status = "borrowed"
            
            if is_returned:
                # 随机生成归还日期（在借阅日期和应还日期之间）
                return_days_ago = random.randint(0, min(30, borrow_days_ago + 30))
                return_date = timezone.now() - timedelta(days=return_days_ago)
                status = "returned"
                book.status = "available"
            else:
                # 检查是否逾期
                if timezone.now() > due_date:
                    status = "overdue"
                book.status = "borrowed"
            
            book.save()
            
            # 创建借阅记录
            borrow_record, created = BorrowRecord.objects.get_or_create(
                user=user,
                book=book,
                borrow_date=borrow_date,
                defaults={
                    "due_date": due_date,
                    "return_date": return_date,
                    "status": status
                }
            )
            
            if created:
                print(f"✅ 创建借阅记录: {user.username} 借阅 {book.name}")
    
    print(f"✅ 共创建了 {BorrowRecord.objects.count()} 条借阅记录")


def main():
    """主函数"""
    print("开始生成模拟数据...")
    print("=" * 50)
    
    # 创建分类
    print("1. 创建图书分类...")
    categories = create_categories()
    print(f"✅ 共创建了 {len(categories)} 个分类")
    print()
    
    # 创建用户
    print("2. 创建用户...")
    users = create_users()
    print(f"✅ 共创建了 {len(users)} 个用户")
    print()
    
    # 创建图书
    print("3. 创建图书...")
    books = create_books(categories)
    print(f"✅ 共创建了 {len(books)} 本图书")
    print()
    
    # 创建借阅记录
    print("4. 创建借阅记录...")
    create_borrow_records(users, books)
    print()
    
    print("=" * 50)
    print("🎉 模拟数据生成完成！")
    print()
    print("数据统计:")
    print(f"- 图书分类: {Category.objects.count()} 个")
    print(f"- 用户: {User.objects.count()} 个")
    print(f"- 图书: {Book.objects.count()} 本")
    print(f"- 借阅记录: {BorrowRecord.objects.count()} 条")
    print()
    print("用户登录信息:")
    print("用户名: admin, 密码: admin123 (超级管理员)")
    for user in users:
        print(f"用户名: {user.username}, 密码: password123")


if __name__ == "__main__":
    main() 