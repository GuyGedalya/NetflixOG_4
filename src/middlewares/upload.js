const multer = require('multer');
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        if (file.fieldname === 'ProfileImage') {
            cb(null, 'uploads/users/');
        } else if (file.fieldname === 'MovieImage') {
            cb(null, 'uploads/movies/images/');
		} else if (file.fieldname === 'Film') {
			cb(null, 'uploads/movies/videos/');
        } else {
            cb(new Error('Invalid field name'), null);
        }
    },
  	filename: (req, file, cb) => {
    	cb(null, Date.now() + '-' + file.originalname);
  	}
});

const upload = multer({
    storage: storage,
    limits: { fileSize: 200 * 1024 * 1024 }, // File size limit: 50MB
    fileFilter: (req, file, cb) => {

        const allowedTypes = {
            ProfileImage: ['image/png', 'image/jpeg', 'image/jpg', 'image/*'],
            MovieImage: ['image/png', 'image/jpeg', 'image/jpg', 'image/*'],
			Film: ['video/mp4', 'video/*']
        };
        const types = allowedTypes[file.fieldname];
        if (types && types.includes(file.mimetype)) {
            cb(null, true);
        } else {
            cb(new Error(`Invalid file type for ${file.fieldname}`));
        }
    }
});

module.exports = upload;
