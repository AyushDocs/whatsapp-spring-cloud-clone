/** @format */

import fileUpload from "express-fileupload";

export const fileMiddleWare = fileUpload({
	createParentPath: true,
});
