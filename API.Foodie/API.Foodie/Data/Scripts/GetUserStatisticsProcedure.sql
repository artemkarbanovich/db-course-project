USE Foodie;


GO
CREATE PROCEDURE dbo.GetUserStatistics
	@userId INT,
	@totalOrdersCount INT OUTPUT,
	@totalMoneySpent MONEY OUTPUT,
	@ordersCountLastMonth INT OUTPUT,
	@moneySpentLastMonth MONEY OUTPUT,
	@waitingOrdersCount INT OUTPUT
AS 
BEGIN
	SELECT @totalOrdersCount = COUNT(*) FROM Orders WHERE Orders.AppUserId = @userId;
	
	SELECT @totalMoneySpent = SUM(Orders.TotalPrice)
	FROM Orders
	WHERE Orders.AppUserId = @userId 
		AND Orders.Status = 'DELIVERED';

	SELECT @ordersCountLastMonth = COUNT(*) 
	FROM Orders 
	WHERE Orders.AppUserId = @userId
		AND Orders.OrderDate > DATEADD(MONTH, -1, GETDATE());

	SELECT @moneySpentLastMonth = SUM(Orders.TotalPrice) 
	FROM Orders 
	WHERE Orders.AppUserId = @userId
		AND Orders.OrderDate > DATEADD(MONTH, -1, GETDATE())
		AND Orders.Status = 'DELIVERED';

	SELECT @waitingOrdersCount = COUNT(*) 
	FROM Orders 
	WHERE Orders.AppUserId = @userId 
		AND Orders.Status IN ('ACCEPTED','IN_WAY');
END
GO
