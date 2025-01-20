const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');

// Setting the routes files:
const users = require('./routes/user');
const categories = require('./routes/categories');
const tokens = require('./routes/token');
const movies = require('./routes/movie');

require('dotenv').config({ path: './config/.env.local' });
mongoose.connect(process.env.CONNECTION_STRING);

var app = express();

app.use(cors());
app.use(bodyParser.urlencoded({extended : true}));

app.use(bodyParser.json({ limit: '50mb' })); 
app.use(bodyParser.urlencoded({ limit: '50mb', extended: true }));

app.use(express.json());

app.use('/api/users', users);
app.use('/api/categories', categories);
app.use('/api/tokens', tokens);
app.use('/api/movies', movies);

app.listen(process.env.PORT);