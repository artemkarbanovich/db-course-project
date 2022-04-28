USE Foodie;


GO
CREATE PROCEDURE dbo.GetAdminStatistics
	@totalUsersCount INT OUT,
	@activeUsersCount INT OUT,
	@totalRevenue MONEY OUT,
	@acceptedOrdersCount INT OUT,
	@inWayOrdersCount INT OUT,
	@deliveredOrdersCount INT OUT,
    @canceledOrdersCount INT OUT,
    @dishesCount INT OUT,
    @visibleDishesCount INT OUT,
    @avgDishPrice MONEY OUT
AS 
BEGIN
	SELECT @totalUsersCount = COUNT(*) FROM AppUsers;
	SELECT @activeUsersCount = COUNT(*) FROM AppUsers 
		WHERE (SELECT COUNT(*) FROM Orders WHERE Orders.AppUserId = AppUsers.Id) > 0;

	SELECT @totalRevenue = SUM(Orders.TotalPrice) FROM Orders;

	SELECT @acceptedOrdersCount = COUNT(*) FROM Orders WHERE Orders.Status = 'ACCEPTED';
	SELECT @inWayOrdersCount = COUNT(*) FROM Orders WHERE Orders.Status = 'IN_WAY';
	SELECT @deliveredOrdersCount = COUNT(*) FROM Orders WHERE Orders.Status = 'DELIVERED';
	SELECT @canceledOrdersCount = COUNT(*) FROM Orders WHERE Orders.Status = 'CANCELED';

	SELECT @dishesCount = COUNT(*) FROM Dishes;
	SELECT @visibleDishesCount = COUNT(*) FROM Dishes WHERE Dishes.IsVisible = 1;
	SELECT @avgDishPrice = AVG(Dishes.Price) FROM Dishes;
END
GO
