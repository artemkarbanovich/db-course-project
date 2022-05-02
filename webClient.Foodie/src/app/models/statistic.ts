export interface Statistic {
    totalUsersCount: number,
    activeUsersCount: number,

    totalRevenue: number,
    avgOrderPrice: number,

    ordersCount: number,
    acceptedOrdersCount: number,
    inWayOrdersCount: number,
    deliveredOrdersCount: number,
    canceledOrdersCount: number,

    dishesCount: number,
    visibleDishesCount: number,
    avgDishPrice: number
}
