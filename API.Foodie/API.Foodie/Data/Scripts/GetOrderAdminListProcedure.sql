USE Foodie;


GO
CREATE PROCEDURE dbo.GetOrderAdminList
	@status NVARCHAR(20)
AS
BEGIN TRY
	DECLARE @statusCommand NVARCHAR(100) = ' WHERE O.Status = ''' + @status + '''';

	IF(@status = 'NOT_MATTER')
		SET @statusCommand = '';

	DECLARE @sqlCommand NVARCHAR(1000) = '
		SELECT
			O.Id,
			O.OrderDate, 
			O.DeliveryDate,
			O.TotalPrice, 
			O.Status, 
			O.Address,
			O.AppUserId,
			AU.FirstName,
			AU.LastName,
			AU.Email,
			AU.PhoneNumber,
			OD.DishesCount,
			D.Name,
			D.CookingTime,
			D.DishWeight,
			D.Price,
			D.Ingredients
		FROM Orders AS O
			JOIN AppUsers AS AU ON O.AppUserId = AU.Id
			JOIN OrderDishes AS OD ON O.Id = OD.OrderId
			JOIN Dishes AS D ON OD.DishId = D.Id'
	  + @statusCommand + ' '
	  + 'ORDER BY O.OrderDate DESC;'
	
	EXEC(@sqlCommand);
END TRY
BEGIN CATCH
	SELECT NULL;
END CATCH
GO
