-- 쿼리문에 개행이 없어야 함 ( 명령어 하나는 무조건 한줄 )

INSERT INTO `main_code` (`main_code_id`, `cd_name`, `description`) VALUES (1, 'role', '권한') ;
INSERT INTO `sub_code` (`sub_code_id`, `main_code_id`, `val`, `description`) VALUES (1, 1, 'ROLE_USER', '일반 사용자'), (2, 1, 'ROLE_ADMIN', '관리자') ;
INSERT INTO `user` (`user_id`,`provider_type`, `uid`, `upw`, `nm`, `pic`, `created_at`, `updated_at`) VALUES(1,4, 'mic1', '$2a$10$gSmS4Bw0wi35PURGxKeq0ua3V0xI20f5NrxelSkSmLXFaPMENt8Qm', '일반사용자', 'a4983740-8a6d-4692-9e17-8b45e1b6f8e4.jpg', '2024-05-3 18:00:47', '2024-07-15 10:14:51') ,(2,4, 'mic2', '$2a$10$gSmS4Bw0wi35PURGxKeq0ua3V0xI20f5NrxelSkSmLXFaPMENt8Qm', '관리자', NULL, '2024-05-03 18:00:55', '2024-07-15 10:14:41'),(3,4, 'mic3', '$2a$10$gSmS4Bw0wi35PURGxKeq0ua3V0xI20f5NrxelSkSmLXFaPMENt8Qm', '사용자_관리자', NULL, '2024-07-15 10:10:42', '2024-07-15 10:14:42') ;
INSERT INTO `user_role` (`user_id`, `role_cd`, `role`) VALUES(1, 1, 'ROLE_USER'), (2, 2, 'ROLE_ADMIN'), (3, 1, 'ROLE_USER'), (3, 2, 'ROLE_ADMIN') ;