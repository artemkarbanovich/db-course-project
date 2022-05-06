USE Foodie;


GO
CREATE PROCEDURE dbo.GetDishUserList
	@nameSearchStr NVARCHAR(25) = '', @orderBy NVARCHAR(15), @orderByType NVARCHAR(15)
AS
BEGIN TRY
	DECLARE @orderByCommand NVARCHAR(100) = '';

	IF(@orderBy = 'NAME')
		SET @orderByCommand = ' ORDER BY Name ' + @orderByType + ';';
	ELSE IF(@orderBy = 'PRICE')
		SET @orderByCommand = ' ORDER BY Price ' + @orderByType + ';';

	DECLARE @sqlCommand NVARCHAR(1000) = '
		SELECT 
			Dishes.Id AS DishId,
			Dishes.Name,
			Dishes.CookingTime,
			Dishes.YouWillNeed,
			Dishes.DishWeight,
			Dishes.Price,
			Dishes.Ingredients,
			Photos.Id AS PhotoId,
			Photos.Url
		FROM Dishes
			JOIN Photos ON Dishes.Id = Photos.DishId
		WHERE 
			(LOWER(Dishes.Name) LIKE LOWER(''%' + @nameSearchStr + '%'')) 
			AND Dishes.IsVisible = 1'
	  + @orderByCommand;

	EXEC(@sqlCommand);
END TRY
BEGIN CATCH
	SELECT NULL;
END CATCH
GO
