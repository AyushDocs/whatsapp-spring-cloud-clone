/** @format */

import { Request } from "express";
import { UploadedFile } from "express-fileupload";
import FormData from "form-data";

export const createFormForFileUpload = (req: Request) => {
	if (!req.files) return undefined;
	const imageFile: UploadedFile | UploadedFile[] = req.files.image;
	if (Array.isArray(imageFile)) return undefined;
	const form = new FormData();
	form.append('image', imageFile.data, imageFile.name);
	return form;
};
