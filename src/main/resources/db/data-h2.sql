-- =============================================
-- Seed Data for Development (H2)
-- =============================================

-- Admin user is created programmatically by DataInitializer.java
-- Login: admin / admin123

-- Food items
INSERT INTO food (name, description, price, category, image_url) VALUES
('Tom Yum Goong', 'Spicy and sour Thai soup with shrimp, mushrooms, lemongrass, and chili. A flavorful explosion of authentic Thai cuisine.', 12.99, 'Soup', NULL),
('Minestrone Soup', 'Traditional Italian vegetable soup with beans, pasta, and fresh herbs. Hearty and nutritious.', 9.99, 'Soup', NULL),
('Mango Lassi', 'Creamy yogurt-based mango smoothie with a hint of cardamom. Refreshingly sweet and cooling.', 5.99, 'Drinks', NULL),
('Thai Iced Tea', 'Strong black tea with condensed milk served over crushed ice. A classic Thai beverage.', 4.99, 'Drinks', NULL),
('Pad Kra Pao', 'Stir-fried Thai basil with minced chicken, chili, garlic, and a fried egg. Fiery and aromatic.', 14.99, 'Spicy', NULL),
('Szechuan Kung Pao Chicken', 'Wok-tossed chicken with peanuts, dried chilies, and Szechuan peppercorns. Bold and numbing.', 15.99, 'Spicy', NULL),
('Nasi Goreng', 'Indonesian fried rice with sweet soy sauce, shrimp paste, shallots, and topped with a fried egg.', 13.99, 'Traditional', NULL),
('Chicken Biryani', 'Fragrant basmati rice layered with spiced chicken, saffron, and caramelized onions. A royal feast.', 16.99, 'Traditional', NULL),
('Mango Sticky Rice', 'Sweet glutinous rice with fresh ripe mango and coconut cream drizzle. A Thai dessert icon.', 8.99, 'Sweet', NULL),
('Churros con Chocolate', 'Crispy fried dough dusted with cinnamon sugar, served with rich dark chocolate dipping sauce.', 7.99, 'Sweet', NULL),
('Green Curry', 'Rich and creamy Thai green curry with bamboo shoots, Thai basil, and tender chicken.', 14.49, 'Spicy', NULL),
('Pho Bo', 'Vietnamese beef noodle soup with aromatic broth, rice noodles, herbs, and bean sprouts.', 13.49, 'Soup', NULL);

-- Sample orders
INSERT INTO orders (customer_name, phone_number, location, status, order_type) VALUES
('Arun Kumar', '0771234567', '123 Main Street, Colombo 03', 'PENDING', 'REGULAR'),
('Sarah Johnson', '0779876543', '45 Beach Road, Mount Lavinia', 'APPROVED', 'REGULAR'),
('David Chen', '0765551234', '78 Temple Lane, Kandy', 'REJECTED', 'REGULAR'),
('Maria Garcia', '0712345678', '12 Lake View, Nuwara Eliya', 'PENDING', 'BUFFET');

-- Order items
INSERT INTO order_items (order_id, food_id, quantity) VALUES
(1, 1, 2),
(1, 3, 1),
(2, 5, 1),
(2, 8, 1),
(2, 9, 2),
(3, 7, 3),
(4, 1, 1),
(4, 5, 1),
(4, 8, 1),
(4, 10, 2);
