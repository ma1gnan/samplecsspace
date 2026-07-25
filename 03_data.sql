-- =====================================================================
-- Real-Estate Office Database — Sample Data
-- Synthetic data only. Run after 02_schema.sql.
-- Run as: psql -d realestate -f 03_data.sql
-- =====================================================================

-- ---------------------------------------------------------------------
-- SCHOOL DISTRICTS
-- ---------------------------------------------------------------------
INSERT INTO school_district (district_name) VALUES
('Parkland'),          -- 1
('Bethlehem Area'),    -- 2
('Easton Area'),       -- 3
('Southern Lehigh'),   -- 4
('Saucon Valley');     -- 5

-- ---------------------------------------------------------------------
-- AGENTS
-- ---------------------------------------------------------------------
INSERT INTO agent (first_name, last_name, phone, email, license_number, hire_date) VALUES
('Angela',  'Reyes',      '610-555-0101', 'areyes@lvrealty.com',      'AG10234', '2015-03-01'),  -- 1
('Brian',   'Kessler',    '610-555-0102', 'bkessler@lvrealty.com',    'AG10456', '2012-07-15'),  -- 2
('Maria',   'Chen',       '610-555-0103', 'mchen@lvrealty.com',       'AG10789', '2018-01-10'),  -- 3
('David',   'Okafor',     '610-555-0104', 'dokafor@lvrealty.com',     'AG11002', '2010-11-20'),  -- 4
('Sofia',   'Marchetti',  '610-555-0105', 'smarchetti@lvrealty.com',  'AG11345', '2020-06-01'),  -- 5
('Thomas',  'Nguyen',     '610-555-0106', 'tnguyen@lvrealty.com',     'AG11678', '2016-09-05');  -- 6

-- ---------------------------------------------------------------------
-- BUYERS
-- ---------------------------------------------------------------------
INSERT INTO buyer (first_name, last_name, phone, email, mailing_address) VALUES
('James',     'Whitfield', '610-555-0201', 'jwhitfield@email.com', '10 Poplar St, Bethlehem PA'),     -- 1
('Linda',     'Choi',      '610-555-0202', 'lchoi@email.com',      '22 Maple Ave, Allentown PA'),      -- 2
('Robert',    'Alvarez',   '610-555-0203', 'ralvarez@email.com',   '5 Birch Ln, Easton PA'),           -- 3
('Karen',     'Muller',    '610-555-0204', 'kmuller@email.com',    '88 Oak Dr, Emmaus PA'),            -- 4
('Steven',    'Park',      '610-555-0205', 'spark@email.com',      '14 Cedar Ct, Allentown PA'),       -- 5
('Patricia',  'Nolan',     '610-555-0206', 'pnolan@email.com',     '31 Elm St, Coopersburg PA'),       -- 6
('Michael',   'Grant',     '610-555-0207', 'mgrant@email.com',     '9 Vista Rd, Bethlehem PA'),        -- 7
('Emily',     'Sanders',   '610-555-0208', 'esanders@email.com',   '47 Ridge Ave, Emmaus PA'),         -- 8
('Daniel',    'Osei',      '610-555-0209', 'dosei@email.com',      '3 Sunset Blvd, Allentown PA'),     -- 9
('Rachel',    'Kim',       '610-555-0210', 'rkim@email.com',       '60 Willow St, Bethlehem PA'),      -- 10
('Anthony',   'Ruiz',      '610-555-0211', 'aruiz@email.com',      '18 Harbor Way, Easton PA'),        -- 11
('Nicole',    'Fischer',   '610-555-0212', 'nfischer@email.com',   '72 Meadow Ln, Allentown PA');      -- 12

-- ---------------------------------------------------------------------
-- SELLERS
-- ---------------------------------------------------------------------
INSERT INTO seller (first_name, last_name, phone, email, mailing_address) VALUES
('George',     'Palmer',    '610-555-0301', 'gpalmer@email.com',    '210 Linden St, Bethlehem PA'),      -- 1
('Susan',      'Ortiz',     '610-555-0302', 'sortiz@email.com',     '15 Overlook Dr, Bethlehem PA'),      -- 2
('William',    'Hess',      '610-555-0303', 'whess@email.com',      '44 Sunrise Ave, Allentown PA'),      -- 3
('Deborah',    'Klein',     '610-555-0304', 'dklein@email.com',     '71 Meadow Ln, Allentown PA'),        -- 4
('Charles',    'Ibe',       '610-555-0305', 'cibe@email.com',       '3 Harbor Way, Easton PA'),           -- 5
('Jennifer',   'Cruz',      '610-555-0306', 'jcruz@email.com',      '500 Ridge Rd, Coopersburg PA'),      -- 6
('Mark',       'Feldman',   '610-555-0307', 'mfeldman@email.com',   '620 Vista Ct, Bethlehem PA'),        -- 7
('Laura',      'Simmons',   '610-555-0308', 'lsimmons@email.com',   '18 Pineview Dr, Emmaus PA'),         -- 8
('Victor',     'Adeyemi',   '610-555-0309', 'vadeyemi@email.com',   '5 Vista Estate Dr, Upper Milford PA'), -- 9
('Christine',  'Adeyemi',   '610-555-0310', 'cadeyemi@email.com',   '5 Vista Estate Dr, Upper Milford PA'), -- 10
('Peter',      'Novak',     '610-555-0311', 'pnovak@email.com',     '9 Stonecrest Ln, Allentown PA'),     -- 11
('Diane',      'Whitfield', '610-555-0312', 'dwhitfield@email.com', '1201 Ridge Ave, Bethlehem PA');      -- 12

-- ---------------------------------------------------------------------
-- PROPERTIES  (property_id assigned in insertion order: 1..25)
-- ---------------------------------------------------------------------
INSERT INTO property
 (street_address, city, zip_code, district_id, bedrooms, bathrooms, square_feet,
  year_built, has_pool, lot_size_acres, list_price, listing_date, status, description, listing_agent_id)
VALUES
-- Bethlehem, $200k-$250k range, FOR SALE  (query a)
('1201 Ridge Ave',      'Bethlehem',     '18018', 2, 3, 2.5, 1850, 1998, FALSE, 0.25, 215000.00, '2004-01-15', 'FOR SALE', 'Charming colonial near Sand Island.', 1),          -- 1  (has photo)
('340 Elm St',          'Bethlehem',     '18017', 2, 3, 2.0, 1600, 1985, FALSE, 0.20, 229900.00, '2004-02-01', 'FOR SALE', 'Updated kitchen, walk to downtown.', 2),          -- 2
('512 Spruce St',       'Bethlehem',     '18015', 2, 4, 2.5, 2100, 2001, FALSE, 0.30, 248500.00, '2004-03-10', 'FOR SALE', 'Move-in ready, fenced yard.', 3),                  -- 3

-- Other Bethlehem-area properties for contrast (outside price range / different district)
('77 Maple Dr',         'Bethlehem',     '18020', 2, 3, 1.5, 1400, 1972, FALSE, 0.18, 189000.00, '2004-01-05', 'FOR SALE', 'Cozy starter home.', 1),                          -- 4
('900 Birch Ln',        'Bethlehem',     '18018', 5, 5, 3.5, 3200, 2010, TRUE,  0.50, 415000.00, '2004-04-01', 'FOR SALE', 'Luxury home, Saucon Valley schools.', 4),         -- 5

-- Parkland School District, 4+ bedrooms, FOR SALE  (query b -- some with pool for contrast)
('88 Chestnut Ct',      'Allentown',     '18104', 1, 4, 2.5, 2400, 2005, FALSE, 0.35, 289000.00, '2004-05-12', 'FOR SALE', 'Open floor plan, 2-car garage.', 2),              -- 6  (has photo)
('12 Hawthorne Rd',     'Allentown',     '18104', 1, 5, 3.0, 2800, 2008, FALSE, 0.40, 325000.00, '2004-06-01', 'FOR SALE', 'Corner lot, finished basement.', 5),               -- 7
('245 Cedar Crest Blvd','Allentown',     '18104', 1, 4, 2.0, 2200, 1999, TRUE,  0.30, 275000.00, '2004-02-20', 'FOR SALE', 'In-ground pool, great for entertaining.', 3),      -- 8  (has pool: excluded from query b)
('60 Trexler Ave',      'Allentown',     '18104', 1, 3, 2.0, 1750, 1995, FALSE, 0.25, 235000.00, '2004-07-01', 'FOR SALE', 'Well maintained ranch.', 6),                       -- 9  (only 3 bed: excluded from query b)
('5 Vista Estate Dr',   'Upper Milford', '18092', 1, 6, 5.5, 6200, 2015, TRUE,  2.50, 1250000.00,'2004-01-20', 'FOR SALE', 'Estate property with mountain views.', 4),         -- 10 (MOST EXPENSIVE, has photo)

-- Easton Area
('33 Northampton St',   'Easton',        '18042', 3, 3, 1.5, 1500, 1965, FALSE, 0.15, 165000.00, '2004-03-01', 'FOR SALE', 'Historic district charmer.', 2),                  -- 11
('401 College Hill Dr', 'Easton',        '18042', 3, 4, 3.0, 2600, 2003, FALSE, 0.30, 289500.00, '2004-04-15', 'FOR SALE', 'Near Lafayette College.', 5),                      -- 12

-- Southern Lehigh
('27 Iron Run Rd',      'Coopersburg',   '18036', 4, 4, 2.5, 2300, 1997, FALSE, 0.40, 259900.00, '2004-05-01', 'FOR SALE', 'Cul-de-sac location.', 1),                        -- 13
('908 Main St',         'Emmaus',        '18049', 4, 3, 2.0, 1700, 1988, FALSE, 0.22, 198500.00, '2004-06-10', 'FOR SALE', 'Walk to Emmaus Triangle.', 6),                     -- 14

-- SOLD in 2004 (queries c and d)
('210 Linden St',       'Bethlehem',     '18018', 2, 3, 2.0, 1650, 1990, FALSE, 0.20, 210000.00, '2004-01-10', 'SOLD', 'Sold above description.', 1),                         -- 15
('15 Overlook Dr',      'Bethlehem',     '18017', 2, 4, 2.5, 2000, 2000, FALSE, 0.28, 245000.00, '2004-02-01', 'SOLD', 'Sold above description.', 1),                         -- 16
('44 Sunrise Ave',      'Allentown',     '18104', 1, 4, 3.0, 2500, 2004, FALSE, 0.35, 310000.00, '2004-01-15', 'SOLD', 'Sold above description.', 4),                         -- 17
('71 Meadow Ln',        'Allentown',     '18104', 1, 5, 3.5, 3000, 2006, TRUE,  0.50, 375000.00, '2004-02-10', 'SOLD', 'Sold above description.', 4),                         -- 18
('3 Harbor Way',        'Easton',        '18042', 3, 3, 2.0, 1600, 1992, FALSE, 0.20, 175000.00, '2004-03-05', 'SOLD', 'Sold above description.', 2),                         -- 19
('500 Ridge Rd',        'Coopersburg',   '18036', 4, 4, 2.5, 2400, 2002, FALSE, 0.40, 265000.00, '2004-04-01', 'SOLD', 'Sold above description.', 6),                         -- 20
('620 Vista Ct',        'Bethlehem',     '18015', 5, 5, 4.0, 3400, 2011, TRUE,  0.60, 450000.00, '2004-01-25', 'SOLD', 'Sold above description.', 4),                         -- 21
('18 Pineview Dr',      'Emmaus',        '18049', 4, 3, 2.0, 1550, 1980, FALSE, 0.20, 189000.00, '2004-05-01', 'SOLD', 'Sold above description.', 3),                         -- 22
('9 Stonecrest Ln',     'Allentown',     '18104', 1, 4, 3.0, 2350, 2003, FALSE, 0.30, 279000.00, '2004-06-15', 'SOLD', 'Sold above description.', 5),                         -- 23

-- Other statuses for variety
('77 Autumn Ridge',     'Bethlehem',     '18020', 2, 3, 2.0, 1700, 1995, FALSE, 0.25, 219000.00, '2004-07-01', 'PENDING', 'Offer accepted, closing pending.', 1),             -- 24
('200 Willow St',       'Bethlehem',     '18017', 2, 4, 2.0, 2050, 1999, FALSE, 0.30, 234500.00, '2005-01-05', 'FOR SALE', 'Newly listed, freshly painted.', 3);              -- 25

-- ---------------------------------------------------------------------
-- PROPERTY OWNERSHIP (bridge table; property 10 shows a joint-owner example)
-- ---------------------------------------------------------------------
INSERT INTO property_owner (property_id, seller_id) VALUES
(1,1), (2,2), (3,3), (4,4), (5,5),
(6,6), (7,7), (8,8), (9,9),
(10,9), (10,10),                 -- joint owners: Victor & Christine Adeyemi
(11,11), (12,12), (13,1), (14,2),
(15,3), (16,4), (17,5), (18,6), (19,7),
(20,8), (21,9), (22,10), (23,11), (24,12), (25,1);

-- ---------------------------------------------------------------------
-- SALES  (all in 2004; selling_agent_id references the listing agent
-- on the corresponding property, buyer_agent_id is optional)
-- ---------------------------------------------------------------------
INSERT INTO sale (property_id, buyer_id, selling_agent_id, buyer_agent_id, sale_price, sale_date) VALUES
(15, 1, 1, 2,    205000.00, '2004-04-20'),
(16, 2, 1, NULL, 240000.00, '2004-05-15'),
(17, 3, 4, 5,    305000.00, '2004-03-30'),
(18, 4, 4, NULL, 368000.00, '2004-06-01'),
(19, 5, 2, 6,    172000.00, '2004-05-25'),
(20, 6, 6, NULL, 260000.00, '2004-07-10'),
(21, 7, 4, 5,    445000.00, '2004-04-10'),
(22, 8, 3, NULL, 185000.00, '2004-08-01'),
(23, 9, 5, 2,    275000.00, '2004-09-10');

-- Agent 2004 sales totals (for reference — verified by query c/d):
--   Angela Reyes (1):    205,000 + 240,000            =   445,000
--   Brian Kessler (2):   172,000                       =   172,000
--   Maria Chen (3):      185,000                       =   185,000
--   David Okafor (4):    305,000 + 368,000 + 445,000   = 1,118,000   <-- top agent by dollar volume
--   Sofia Marchetti (5): 275,000                       =   275,000
--   Thomas Nguyen (6):   260,000                       =   260,000
