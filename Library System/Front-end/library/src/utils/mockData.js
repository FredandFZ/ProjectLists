// 模拟统计数据
export const mockDashboardData = {
  users: {
    total: 150,
    active: 145,
    staff: 5
  },
  books: {
    total: 1000,
    available: 850,
    borrowed: 150
  },
  categories: {
    total: 20
  },
  borrow_records: {
    total: 500,
    active: 150,
    returned: 320,
    overdue: 30
  },
  today: {
    borrows: 15,
    returns: 8
  }
}

export const mockBookStats = {
  categories: [
    { id: 1, name: '文学小说', count: 250 },
    { id: 2, name: '科技技术', count: 180 },
    { id: 3, name: '历史传记', count: 120 },
    { id: 4, name: '艺术设计', count: 95 },
    { id: 5, name: '经济管理', count: 150 },
    { id: 6, name: '教育考试', count: 80 },
    { id: 7, name: '生活健康', count: 75 },
    { id: 8, name: '其他', count: 50 }
  ],
  popularBooks: [
    { id: 1, title: '三体', author: '刘慈欣', borrowCount: 45 },
    { id: 2, title: '百年孤独', author: '加西亚·马尔克斯', borrowCount: 38 },
    { id: 3, title: '活着', author: '余华', borrowCount: 32 },
    { id: 4, title: '1984', author: '乔治·奥威尔', borrowCount: 28 },
    { id: 5, title: '红楼梦', author: '曹雪芹', borrowCount: 25 }
  ],
  recentBooks: [
    { id: 101, title: '新书1', author: '作者1', addDate: '2024-01-15' },
    { id: 102, title: '新书2', author: '作者2', addDate: '2024-01-14' },
    { id: 103, title: '新书3', author: '作者3', addDate: '2024-01-13' }
  ]
}

export const mockBorrowStats = {
  trend: [
    { date: '2024-01-01', borrows: 12, returns: 8 },
    { date: '2024-01-02', borrows: 15, returns: 10 },
    { date: '2024-01-03', borrows: 18, returns: 12 },
    { date: '2024-01-04', borrows: 14, returns: 9 },
    { date: '2024-01-05', borrows: 20, returns: 15 },
    { date: '2024-01-06', borrows: 16, returns: 11 },
    { date: '2024-01-07', borrows: 22, returns: 18 },
    { date: '2024-01-08', borrows: 19, returns: 14 },
    { date: '2024-01-09', borrows: 17, returns: 13 },
    { date: '2024-01-10', borrows: 21, returns: 16 },
    { date: '2024-01-11', borrows: 24, returns: 19 },
    { date: '2024-01-12', borrows: 18, returns: 12 },
    { date: '2024-01-13', borrows: 16, returns: 10 },
    { date: '2024-01-14', borrows: 20, returns: 15 },
    { date: '2024-01-15', borrows: 23, returns: 17 }
  ],
  overdue: {
    total: 30,
    percentage: 6
  },
  timeRange: {
    start: '2024-01-01',
    end: '2024-01-15',
    days: 15
  }
}

export const mockUserStats = {
  registrationTrend: [
    { date: '2024-01-01', count: 3 },
    { date: '2024-01-02', count: 5 },
    { date: '2024-01-03', count: 2 },
    { date: '2024-01-04', count: 7 },
    { date: '2024-01-05', count: 4 },
    { date: '2024-01-06', count: 6 },
    { date: '2024-01-07', count: 8 },
    { date: '2024-01-08', count: 3 },
    { date: '2024-01-09', count: 5 },
    { date: '2024-01-10', count: 9 },
    { date: '2024-01-11', count: 4 },
    { date: '2024-01-12', count: 6 },
    { date: '2024-01-13', count: 7 },
    { date: '2024-01-14', count: 3 },
    { date: '2024-01-15', count: 5 }
  ],
  userTypes: [
    { type: '学生', count: 120 },
    { type: '教师', count: 15 },
    { type: '职工', count: 10 },
    { type: '其他', count: 5 }
  ],
  activeUsers: [
    { id: 1, name: '张三', borrowCount: 25, lastActive: '2024-01-15' },
    { id: 2, name: '李四', borrowCount: 22, lastActive: '2024-01-14' },
    { id: 3, name: '王五', borrowCount: 20, lastActive: '2024-01-13' },
    { id: 4, name: '赵六', borrowCount: 18, lastActive: '2024-01-12' },
    { id: 5, name: '钱七', borrowCount: 16, lastActive: '2024-01-11' }
  ],
  recentUsers: [
    { id: 101, name: '新用户1', registerDate: '2024-01-15' },
    { id: 102, name: '新用户2', registerDate: '2024-01-14' },
    { id: 103, name: '新用户3', registerDate: '2024-01-13' }
  ]
}

export const mockComprehensiveData = {
  overview: {
    totalBooks: 1000,
    totalUsers: 150,
    totalBorrows: 500,
    totalCategories: 20
  },
  today: {
    newBooks: 3,
    newUsers: 2,
    totalBorrows: 15,
    totalReturns: 8
  },
  monthly: {
    newBooks: 45,
    newUsers: 28,
    totalBorrows: 320,
    overdueBooks: 30
  },
  ratios: {
    bookUtilization: 85,
    userActivity: 96.7,
    returnRate: 94.1,
    overdueRate: 6
  },
  popularCategories: [
    { id: 1, name: '文学小说', bookCount: 250, borrowCount: 180 },
    { id: 2, name: '科技技术', bookCount: 180, borrowCount: 150 },
    { id: 3, name: '经济管理', bookCount: 150, borrowCount: 120 },
    { id: 4, name: '历史传记', bookCount: 120, borrowCount: 95 },
    { id: 5, name: '艺术设计', bookCount: 95, borrowCount: 75 }
  ]
}

// 模拟API响应
export const createMockResponse = (data) => ({
  success: true,
  data: data
})

// 模拟API延迟
export const mockApiDelay = (ms = 500) => {
  return new Promise(resolve => setTimeout(resolve, ms))
} 