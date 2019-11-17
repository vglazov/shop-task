create database shop;
create user 'shop'@'%' IDENTIFIED BY 'shop';
grant select, insert, update, delete on shop.* to 'shop'@'%';

use shop;

create table products(
    id int auto_increment primary key,
    name varchar(50),
    price decimal(10, 2)
);

create table orders(
    id int auto_increment primary key,
    date_created timestamp,
    total decimal(15, 2)
);

create table order_positions(
    id int auto_increment primary key,
    order_id int,
    product_id int,
    price decimal(10, 2),
    quantity int,
    foreign key (order_id) references orders(id) on delete cascade,
    foreign key (product_id) references products(id) on delete restrict
);
