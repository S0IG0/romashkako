ALTER TABLE IF EXISTS sale DROP CONSTRAINT sale_price_check;
ALTER TABLE sale ADD CONSTRAINT sale_cost_check CHECK (cost >= 0.00);
