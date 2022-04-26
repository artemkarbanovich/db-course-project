USE Foodie;


GO
CREATE TYPE dbo.OrderDishes
AS TABLE
(
  DishId INT,
  DishesCount INT
);

GO
CREATE PROCEDURE dbo.CalculateOrderTotalPrice
	@orderDishes AS dbo.OrderDishes READONLY
AS
BEGIN TRY
	SELECT SUM(OD.DishesCount * Dishes.Price)
	FROM @orderDishes AS OD JOIN Dishes ON OD.DishId = Dishes.Id;
END TRY
BEGIN CATCH
	SELECT -1;
END CATCH
GO
