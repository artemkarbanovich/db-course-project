USE Foodie;

GO
CREATE TYPE dbo.OrderDishes
AS TABLE
(
  Price MONEY,
  DishesCount INT
);


GO
CREATE PROCEDURE dbo.CalculateOrderTotalPrice
	@orderDishes AS dbo.OrderDishes READONLY
AS
BEGIN TRY
	SELECT SUM(Price * DishesCount)
	FROM @orderDishes;
END TRY
BEGIN CATCH
	SELECT -1;
END CATCH
GO
