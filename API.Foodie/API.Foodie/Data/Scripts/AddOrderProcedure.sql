USE Foodie;


GO
CREATE PROCEDURE dbo.AddOrder
	@orderDate SMALLDATETIME, @deliveryDate SMALLDATETIME,
	@totalPrice MONEY, @status NVARCHAR(25), @address NVARCHAR(120),
	@appUserId INT, @dishes AS dbo.OrderDishes READONLY
AS
BEGIN TRY
	BEGIN TRAN
		DECLARE @orderId INT = -1;

		INSERT INTO Orders (OrderDate, DeliveryDate, TotalPrice, Status, Address, AppUserId)
		VALUES (@orderDate, @deliveryDate, @totalPrice, @status, @address, @appUserId);

		SET @orderId = CAST(SCOPE_IDENTITY() AS INT);

		IF (@orderId <= 0) 
			THROW 00002, 'Error by making order', 1;

		INSERT INTO OrderDishes (DishesCount, OrderId, DishId)
		SELECT D.DishesCount, @orderId, D.DishId
		FROM @dishes AS D;

	COMMIT TRAN;
	SELECT 1;
END TRY
BEGIN CATCH
	IF (@@TRANCOUNT > 0)
		ROLLBACK TRAN;

	SELECT 0;
END CATCH
GO
