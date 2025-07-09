-- Create users table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
CREATE TABLE dbo.users (
	id int IDENTITY(1,1) NOT NULL,
	address varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	email varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	name varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	password varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	phone varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[role] varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	CONSTRAINT PK__users__3213E83F628A9D8F PRIMARY KEY (id),
	CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
);
END
GO

-- Create category table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='category' AND xtype='U')
BEGIN
CREATE TABLE dbo.category (
	id bigint IDENTITY(1,1) NOT NULL,
	name varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	CONSTRAINT PK__category__3213E83FFD3D981B PRIMARY KEY (id)
);
END
GO

-- Create product table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='product' AND xtype='U')
BEGIN
CREATE TABLE dbo.product (
	id bigint IDENTITY(1,1) NOT NULL,
	description nvarchar(MAX) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	image_url varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	name varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	price numeric(10,2) NOT NULL,
	stock int NOT NULL,
	category_id bigint NOT NULL,
	CONSTRAINT PK__product__3213E83F585A50A7 PRIMARY KEY (id)
);
ALTER TABLE dbo.product ADD CONSTRAINT FK1mtsbur82frn64de7balymq9s FOREIGN KEY (category_id) REFERENCES dbo.category(id);
END
GO

-- Insert admin user
IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'admin@admin.com')
BEGIN
    INSERT INTO dbo.users (
        address,
        email,
        name,
        password,
        phone,
        [role]
    ) VALUES (
        'ADDRESS',
        'admin@admin.com',
        'Admin 001',
        '$2a$10$JIw83fXfxfsrH2NpvKqjd.HUWlw6zpKuz7t5qYYEXFZ2tMouZyB/.',
        '0909090909',
        'ADMIN'
    );
END
GO

-- Insert category record
IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE name = 'Waxworks')
BEGIN
    INSERT INTO dbo.category (name) VALUES ('Waxworks');
END
GO

-- Insert product records linked to Waxworks category
DECLARE @waxworks_id BIGINT;
SELECT @waxworks_id = id FROM dbo.category WHERE name = 'Waxworks';

IF @waxworks_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM dbo.product WHERE name = 'HOPO Golden Orchard – Peach')
BEGIN
    INSERT INTO dbo.product (description, image_url, name, price, stock, category_id) VALUES
    ('Bright, juicy, and playfully sweet. This fragrance glows like a peach grove at sunset—summer bottled in wax.', '/uploads/1751899312816_1peach.png', 'HOPO Golden Orchard – Peach', 19.99, 46, @waxworks_id),
    ('Elegant and poetic, this candle wraps your space in the timeless allure of blooming roses. A floral breeze to warm the heart.', '/uploads/1751899360976_1rose.png', 'HOPO Bloom Whisper – Rose', 18.99, 47, @waxworks_id),
    ('Tranquil and earthy, this scent offers a mindful escape. Green tea''s herbal calm brings peace to your senses.', '/uploads/1751899923070_1tea.png', 'HOPO Zen Leaves – Green Tea', 20.99, 47, @waxworks_id),
    ('Velvety and indulgent, this fragrance swirls with rich chocolatey warmth—like a hug made of cocoa.', '/uploads/1751900001121_1choco.png', 'HOPO Cocoa Ember – Chocolate', 18.99, 39, @waxworks_id),
    ('Fresh and breezy, this candle pulls you toward the sea with a splash of marine air and coastal serenity.', '/uploads/1751900314290_1ocean.png', 'HOPO Ocean Drift – Ocean', 19.99, 46, @waxworks_id);
END
GO 