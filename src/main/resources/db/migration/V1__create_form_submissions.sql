CREATE TABLE form_submissions (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    company VARCHAR(100) NOT NULL,
    role VARCHAR(100),
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(50),
    locations VARCHAR(255),
    charger_types TEXT,
    description TEXT,
    consent BOOLEAN DEFAULT FALSE,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
