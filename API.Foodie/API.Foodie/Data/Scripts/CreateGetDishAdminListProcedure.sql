USE Foodie;


GO
CREATE PROCEDURE dbo.GetDishAdminList
	@nameSearchStr NVARCHAR(25), @orderBy NVARCHAR(15),
	@orderByType NVARCHAR(15), @isVisible NVARCHAR(15)
AS
BEGIN TRY
	DECLARE @isVisibleCommand NVARCHAR(100) = '';
	DECLARE @orderByCommand NVARCHAR(100) = '';

	IF(@isVisible = 'TRUE')
		SET @isVisibleCommand = ' AND Dishes.IsVisible = 1';
	ELSE IF (@isVisible = 'FALSE')
		SET @isVisibleCommand = ' AND Dishes.IsVisible = 0';

	IF(@orderBy = 'NAME')
		SET @orderByCommand = ' ORDER BY Name ' + @orderByType + ';';
	ELSE IF(@orderBy = 'PRICE')
		SET @orderByCommand = ' ORDER BY Price ' + @orderByType + ';';

	DECLARE @sqlCommand NVARCHAR(1000) = '
		SELECT Dishes.Id AS DishId, Dishes.Name, Dishes.Price, Photos.Id AS PhotoId, Photos.Url
		FROM Dishes JOIN Photos ON Dishes.Id = Photos.DishId
		WHERE (LOWER(Dishes.Name) LIKE LOWER(''%' + @nameSearchStr + '%''))'
	  + @isVisibleCommand
	  + @orderByCommand;

	EXEC(@sqlCommand);
END TRY
BEGIN CATCH
	SELECT NULL;
END CATCH
GO
