#!/usr/bin/env python
"""
统计接口测试脚本
用于测试图书馆管理系统的统计接口功能
"""

import requests
import json
from datetime import datetime

# 配置
BASE_URL = "http://localhost:8000"
ADMIN_USERNAME = "admin"
ADMIN_PASSWORD = "admin123"

def login():
    """管理员登录"""
    login_data = {
        "username": ADMIN_USERNAME,
        "password": ADMIN_PASSWORD
    }
    
    response = requests.post(f"{BASE_URL}/api/auth/login", json=login_data)
    if response.status_code == 200:
        data = response.json()
        return data.get('access_token')
    else:
        print(f"登录失败: {response.status_code}")
        print(response.text)
        return None

def test_statistics_api(token):
    """测试统计接口"""
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    # 测试接口列表
    test_endpoints = [
        "/api/statistics/dashboard",
        "/api/statistics/books", 
        "/api/statistics/borrows",
        "/api/statistics/users",
        "/api/statistics/comprehensive"
    ]
    
    print("=" * 60)
    print("统计接口测试结果")
    print("=" * 60)
    
    for endpoint in test_endpoints:
        print(f"\n测试接口: {endpoint}")
        print("-" * 40)
        
        try:
            response = requests.get(f"{BASE_URL}{endpoint}", headers=headers)
            
            if response.status_code == 200:
                data = response.json()
                if data.get('success'):
                    print("✅ 接口调用成功")
                    print(f"数据概要:")
                    
                    # 根据接口类型显示不同的数据概要
                    if 'dashboard' in endpoint:
                        stats = data['data']
                        print(f"  用户总数: {stats['users']['total']}")
                        print(f"  图书总数: {stats['books']['total']}")
                        print(f"  今日借阅: {stats['today']['borrows']}")
                        
                    elif 'books' in endpoint:
                        stats = data['data']
                        print(f"  分类数量: {len(stats['category_distribution'])}")
                        print(f"  热门图书: {len(stats['popular_books'])}")
                        
                    elif 'borrows' in endpoint:
                        stats = data['data']
                        print(f"  统计天数: {stats['time_range']['days']}")
                        print(f"  逾期率: {stats['overdue_stats']['overdue_rate']}%")
                        
                    elif 'users' in endpoint:
                        stats = data['data']
                        print(f"  活跃用户: {len(stats['active_users'])}")
                        print(f"  用户总数: {stats['user_type_distribution']['total']}")
                        
                    elif 'comprehensive' in endpoint:
                        stats = data['data']
                        print(f"  综合概览: {stats['overview']}")
                        print(f"  今日数据: {stats['today']}")
                        
                else:
                    print("❌ 接口返回错误")
                    print(f"错误信息: {data.get('error')}")
            else:
                print(f"❌ HTTP错误: {response.status_code}")
                print(f"响应内容: {response.text}")
                
        except Exception as e:
            print(f"❌ 请求异常: {str(e)}")
    
    print("\n" + "=" * 60)
    print("测试完成")
    print("=" * 60)

def test_with_parameters(token):
    """测试带参数的统计接口"""
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    
    print("\n测试带参数的借阅统计接口")
    print("-" * 40)
    
    # 测试不同的天数参数
    test_days = [7, 14, 30, 90]
    
    for days in test_days:
        print(f"\n测试 {days} 天的统计数据:")
        try:
            response = requests.get(
                f"{BASE_URL}/api/statistics/borrows?days={days}", 
                headers=headers
            )
            
            if response.status_code == 200:
                data = response.json()
                if data.get('success'):
                    stats = data['data']
                    print(f"  ✅ 成功获取 {days} 天数据")
                    print(f"  时间范围: {stats['time_range']['start_date']} 到 {stats['time_range']['end_date']}")
                    print(f"  数据点数量: {len(stats['daily_borrows'])}")
                else:
                    print(f"  ❌ 接口返回错误: {data.get('error')}")
            else:
                print(f"  ❌ HTTP错误: {response.status_code}")
                
        except Exception as e:
            print(f"  ❌ 请求异常: {str(e)}")

def main():
    """主函数"""
    print("图书馆管理系统统计接口测试")
    print("=" * 60)
    
    # 登录获取token
    print("正在登录...")
    token = login()
    
    if not token:
        print("登录失败，无法继续测试")
        return
    
    print("登录成功，开始测试统计接口...")
    
    # 测试基础统计接口
    test_statistics_api(token)
    
    # 测试带参数的接口
    test_with_parameters(token)
    
    print("\n所有测试完成！")

if __name__ == "__main__":
    main() 