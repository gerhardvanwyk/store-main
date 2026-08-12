const N = 100; // Number of customers
const P = 200; // Number of products
const M = 10_000; // Number of orders

const firstNames = ['Muriel', 'Lance', 'Denise', 'Dianne', 'Jean', 'Vicki', 'Winifred', 'Robin', 'Leticia', 'Natalie'];
const lastNames = ['Donnelly', 'Stiedemann', 'Harris', 'Lemke', 'Daniel', 'Kutch', 'Morissette', 'Steuber', 'MacGyver', 'Oberbrunner'];
const productAdjectives = ['Handcrafted', 'Small', 'Refined', 'Intelligent', 'Ergonomic', 'Rustic', 'Practical', 'Fantastic', 'Incredible', 'Awesome'];
const productMaterials = ['Steel', 'Wooden', 'Concrete', 'Plastic', 'Cotton', 'Granite', 'Rubber', 'Leather', 'Soft', 'Fresh'];
const productTypes = ['Chair', 'Table', 'Car', 'Computer', 'Gloves', 'Pants', 'Shirt', 'Shoes', 'Hat', 'Pizza'];

function getRandom(array) {
    return array[Math.floor(Math.random() * array.length)];
}

function getFullName() {
    return `${getRandom(firstNames)} ${getRandom(lastNames)}`;
}

function getProductName() {
    return `${getRandom(productAdjectives)} ${getRandom(productMaterials)} ${getRandom(productTypes)}`;
}

// Generate customers
for (let i = 1; i <= N; i++) {
    console.log(`INSERT INTO customer (id, name) VALUES (${i}, '${getFullName().replace(/'/g, "''")}');`);
}

// Generate products
for (let i = 1; i <= P; i++) {
    console.log(`INSERT INTO product (id, description) VALUES (${i}, '${getProductName().replace(/'/g, "''")}');`);
}

// Generate orders and associations
for (let i = 1; i <= M; i++) {
    console.log(`INSERT INTO "order" (id, description) VALUES (${i}, '${getProductName().replace(/'/g, "''")}');`);
    
    // Associate with 1-3 random customers
    const numCustomers = Math.floor(Math.random() * 3) + 1;
    const customers = new Set();
    while (customers.size < numCustomers) {
        customers.add(Math.ceil(Math.random() * N));
    }
    customers.forEach(customerId => {
        console.log(`INSERT INTO order_customer (order_id, customer_id) VALUES (${i}, ${customerId});`);
    });

    // Associate with 1-5 random products
    const numProducts = Math.floor(Math.random() * 5) + 1;
    const products = new Set();
    while (products.size < numProducts) {
        products.add(Math.ceil(Math.random() * P));
    }
    products.forEach(productId => {
        console.log(`INSERT INTO order_product (order_id, product_id) VALUES (${i}, ${productId});`);
    });
}
