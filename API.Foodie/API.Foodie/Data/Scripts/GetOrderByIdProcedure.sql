USE Foodie;


GO
CREATE PROCEDURE dbo.GetOrderById
	@orderId INT
AS 
BEGIN
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
		D.YouWillNeed,
		D.DishWeight,
		D.Price,
		D.Ingredients
	FROM Orders AS O
		JOIN AppUsers AS AU ON O.AppUserId = AU.Id
		JOIN OrderDishes AS OD ON O.Id = OD.OrderId
		JOIN Dishes AS D ON OD.DishId = D.Id
	WHERE O.Id = @orderId;
END
GO
