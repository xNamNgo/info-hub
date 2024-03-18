use info_hub;
SET SQL_SAFE_UPDATES = 0;

INSERT INTO `role` VALUES (1,'ROLE_USER','Người dùng'),(2,'ROLE_ADMIN','Quản trị viên'),(3,'ROLE_COLLABORATOR','Cộng tác viên'),(4,'ROLE_JOURNALIST','Nhà báo');
INSERT INTO `user` VALUES (1,'2024-03-04 15:44:11.784186','hoangnam11pdp@gmail.com',NULL,'Ngô Hoàng Nam',_binary '','$2a$10$/V4E03YtTVkyzX0jKCwHh.T/oaVakw4FhQg8RRJ7/fmLDug7V5elS',NULL,'2024-03-04 15:44:11.784186',2),(2,'2024-03-04 20:56:52.055648','admin@gmail.com',NULL,'admin',_binary '','$2a$10$nsUvMCrfIy2C/Syf05dNu.4hxf4D3pf6nz7bLt/GAiS3sVNt4Ih9y',NULL,'2024-03-04 20:56:52.055648',2),(3,'2024-03-06 15:36:56.594441','reviewer1',NULL,'reviewer_1',_binary '','$2a$10$zHJvzyysXW/w4cOAdjFxZOkJE1wVPFrp1boo7a8Q/EGxCl1SmtqkG',NULL,'2024-03-06 15:36:56.594441',3);
INSERT INTO `category` VALUES (1,1,'2024-03-06 15:18:18.913000',NULL,NULL,'thoi-su','Thời sự',NULL),(2,1,'2024-03-06 15:18:28.858000',NULL,NULL,'the-gioi','Thế giới',NULL),(3,1,'2024-03-06 15:18:43.740000',NULL,NULL,'kinh-te','Kinh tế',NULL),(4,1,'2024-03-06 15:18:52.481000',NULL,NULL,'doi-song','Đời sống',NULL),(5,1,'2024-03-06 15:19:02.586000',NULL,NULL,'suc-khoe','Sức khoẻ',NULL),(6,1,'2024-03-06 15:19:08.418000',NULL,NULL,'gioi-tre','Giới trẻ',NULL),(7,1,'2024-03-06 15:19:15.969000',NULL,NULL,'giao-duc','Giáo dục',NULL),(8,1,'2024-03-06 15:19:23.570000',NULL,NULL,'du-lich','Du lịch',NULL),(9,1,'2024-03-06 15:19:32.770000',NULL,NULL,'van-hoa','Văn hoá',NULL),(10,1,'2024-03-06 15:19:42.346000',NULL,NULL,'the-thao','Thể thao',NULL),(11,1,'2024-03-06 15:19:47.495000',NULL,NULL,'giai-tri','Giải trí',NULL),(12,1,'2024-03-06 15:19:59.020000',NULL,NULL,'cong-nghe','Công nghệ',NULL),(13,1,'2024-03-06 15:21:45.168000',NULL,NULL,'chinh-tri','Chính trị',1),(14,1,'2024-03-06 15:21:53.136000',NULL,NULL,'dan-sinh','Dân sinh',1),(15,1,'2024-03-06 15:22:06.772000',NULL,NULL,'phap-luat','Pháp luật',1),(16,1,'2024-03-06 15:22:19.973000',NULL,NULL,'lao-dong-viec-lam','Lao động - việc làm',1),(17,1,'2024-03-06 15:22:39.221000',NULL,NULL,'goc-nhin','Góc nhìn',2),(18,1,'2024-03-06 15:22:50.697000',NULL,NULL,'chuyen-la','Chuyện lạ',2);
INSERT INTO `tag` VALUES (1,1,'2024-03-06 15:28:50.953000',NULL,NULL,'meta','META'),(2,1,'2024-03-06 15:29:02.496000',NULL,NULL,'mang-xa-hoi','MẠNG XÃ HỘI'),(3,1,'2024-03-06 15:29:18.893000',NULL,NULL,'meme','MEME'),(4,1,'2024-03-06 15:29:32.735000',NULL,NULL,'hinh-anh-hai-huoc','Hình ảnh hài hước');


-- to replace ip
UPDATE image
SET url = REPLACE(url, '192.168.16.255:8080/', 'testting:8080/')
WHERE url LIKE 'http://192.168.16.255:8080/api/media/%';
