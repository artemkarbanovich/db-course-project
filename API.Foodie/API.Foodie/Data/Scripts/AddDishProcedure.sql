USE Foodie;


GO
CREATE TYPE dbo.PhotoList
AS TABLE
(
  Url NVARCHAR(100),
  PublicId NVARCHAR(200)
);

GO
CREATE PROCEDURE dbo.AddDish
	@name NVARCHAR(25), @cookingTime TIME, @youWillNeed NVARCHAR(120),
	@dishWeight INT, @price MONEY, @ingredients NVARCHAR(120),
	@isVisible BIT, @list AS dbo.PhotoList READONLY
AS
BEGIN TRY
	BEGIN TRAN
		DECLARE @dishId INT = -1;

		INSERT INTO Dishes (Name, CookingTime, YouWillNeed, DishWeight, Price, Ingredients, IsVisible)
		VALUES (@name, @cookingTime, @youWillNeed, @dishWeight, @price, @ingredients, @isVisible);

		SET @dishId = CAST(SCOPE_IDENTITY() AS INT);

		IF (@dishId <= 0) 
			THROW 00001, 'Error by adding dish', 1;

		INSERT INTO Photos (Url, PublicId, DishId)
		SELECT l.Url, l.PublicId, @dishId
		FROM @list AS l;
	COMMIT TRAN;
	SELECT 1;
END TRY
BEGIN CATCH
	IF (@@TRANCOUNT > 0)
		ROLLBACK TRAN;

	SELECT 0;
END CATCH
GO
