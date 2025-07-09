IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'ecommerce_app')
BEGIN
    CREATE DATABASE [ecommerce_app];
END
GO
