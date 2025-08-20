drop table if exists user;
create table user
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    name              varchar(20)       not null unique            comment '昵称',
    gender            tinyint unsigned  not null                   comment '性别(0:男/1:女)',
    birthday          date              not null                   comment '生日',
    phone_number      varchar(20)       not null unique            comment '手机号码',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '用户信息表';

drop table if exists shop;
create table shop
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    shop_type         tinyint unsigned  not null                   comment '店铺类型(0:普通/1:旗舰店)',
    name              varchar(20)       not null                   comment '店铺名称',
    user_id           bigint(20)        not null                   comment '创建者ID',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `shop_fk_user_id` foreign key (user_id) references user(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '店铺信息表';

drop table if exists goods;
create table goods
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    name              varchar(50)       not null                   comment '商品名称',
    price             double            not null                   comment '商品原价',
    discount          double            not null                   comment '商品折扣',
    goods_type        varchar(20)       not null                   comment '商品款式',
    goods_brand       varchar(20)       not null                   comment '商品品牌',
    collage           int(11)           default 0                  comment '已拼件数',
    picture           varchar(200)      default null               comment '商品图片',
    if_check          tinyint unsigned  not null                   comment '是否质检(0:质检/1:未质检)',
    if_delete         tinyint unsigned  not null                   comment '是否删除(0:未删除/1:已删除)',
    shop_id           bigint(20)        not null                   comment '所属店铺', 
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `goods_fk_shop_id` foreign key (shop_id) references shop(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '商品信息表';

drop table if exists orders;
create table orders
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    goods_id          bigint(20)        not null                   comment '商品ID',
    count             int(11)           default 1                  comment '商品数量',
    if_discount       tinyint unsigned  default 0                  comment '是否打折(0:不打折/1:打折)',
    pay_status        tinyint unsigned  not null                   comment '支付状态(0:未支付/1:已支付)',
    user_id           bigint(20)        not null                   comment '订单用户ID',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `orders_fk_goods_id` foreign key (goods_id) references goods(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '订单信息表';

drop table if exists address;
create table address
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    name              varchar(20)       not null                   comment '收货人',
    phone_number      varchar(20)       not null                   comment '手机号',
    region            varchar(20)       not null                   comment '地区',
    detailed_address  varchar(50)       not null                   comment '详细地址',
    delivery_status   tinyint unsigned  default 0                  comment '是否为默认地址(0:默认/1:不默认)',
    if_delete         tinyint unsigned  not null                   comment '是否删除(0:未删除/1:已删除)',
    user_id           bigint(20)        not null                   comment '用户ID',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `address_fk_user_id` foreign key (user_id) references user(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '地址信息表';

drop table if exists delivery;
create table delivery
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    order_id          bigint(20)        not null unique            comment '订单编号',
    address_id        bigint            not null                   comment '收件地址信息',
    delivery_name     varchar(20)       not null                   comment '快递名称',
    location          varchar(20)       not null                   comment '物流状态',
    delivery_phone_number varchar(20)   not null                   comment '快递员电话',
    status            tinyint unsigned  default 0                  comment '物流状态(0:未发货/1:已发货/2:已签收)',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `delivery_fk_order_id` foreign key (order_id) references orders(id),
    constraint delivery_fk_address_id foreign key (address_id) references address (id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '物流信息表';

drop table if exists comments;
create table comments
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    user_id           bigint(20)        not null                   comment '评论用户ID',
    goods_id          bigint(20)        not null                   comment '商品ID',
    content           varchar(300)      not null                   comment '评论内容',
    picture           varchar(250)      default null               comment '评论图片',
    if_anonymous      tinyint unsigned  default 0                  comment '是否匿名(0:匿名/1:不匿名)',
    if_appended       bigint(20)        default 0                  comment '是否为追评(0:否/其他:追评ID)',
    create_time       datetime          default current_timestamp  comment '创建时间',
    update_time       datetime          default current_timestamp  comment '最近更新时间',
    primary key (id),
    constraint `comments_fk_user_id` foreign key (user_id) references user(id),
    constraint `comments_fk_goods_id` foreign key (goods_id) references goods(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '评论信息表';

drop table if exists password;
create table password
(
    id                bigint(20)        not null auto_increment    comment 'ID',
    user_id           bigint(20)        not null unique            comment '用户ID',
    password          varchar(100)      not null                   comment '密码',
    primary key (id),
    constraint `password_fk_user_id` foreign key (user_id) references user(id)
) engine=innodb DEFAULT CHARSET=utf8 comment = '密码信息表';