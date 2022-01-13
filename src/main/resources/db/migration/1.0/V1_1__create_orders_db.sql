
CREATE TABLE IF NOT EXISTS users(
    id bigint primary key GENERATED ALWAYS AS IDENTITY,
    username text not null,
    password text not null,
    dollar_balance numeric,
    enabled boolean not null default true,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp
) ;

create table IF NOT EXISTS user_stock_balances(
    id_user bigint references users(id),
    id_stock bigint not null,
    stock_symbol text not null,
    stock_name text not null,
    volume bigint not null default 0,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp,
    primary key (id_user, id_stock)
);

create table IF NOT EXISTS user_orders(
    id bigint primary key GENERATED ALWAYS AS IDENTITY,
    id_user bigint references users(id),
    id_stock bigint not null,
    stock_symbol text not null,
    stock_name text not null,
    volume bigint not null default 0,
    remaining_volume bigint not null default 0,
    price numeric not null default 0.0,
    total_price numeric not null default 0.0,
    type int not null,
    status int not null,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp
);


