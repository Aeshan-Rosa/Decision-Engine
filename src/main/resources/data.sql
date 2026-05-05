DELETE FROM decision_history;
DELETE FROM choices;
DELETE FROM events;

INSERT INTO events (id, title, description, stage, category, min_intelligence, min_risk, max_health, min_age, max_age) VALUES
(1,'Final Exam Week','Your final exams are coming. What do you do?','SCHOOL','SCHOOL',NULL,NULL,NULL,14,17),
(2,'Science Fair','A teacher invites you to the science fair.','SCHOOL','SCHOOL',NULL,NULL,NULL,14,17),
(3,'Part-Time Tutoring','A neighbor asks you to tutor their child.','SCHOOL','MONEY',NULL,NULL,NULL,15,18),
(4,'Sports Tournament','Your school team needs one more player.','SCHOOL','HEALTH',NULL,NULL,NULL,14,18),
(5,'Scholarship Test','A scholarship exam is announced.','SCHOOL','UNIVERSITY',75,NULL,NULL,16,19),
(6,'Campus Club Fair','University clubs are recruiting newcomers.','UNIVERSITY','UNIVERSITY',NULL,NULL,NULL,18,23),
(7,'Roommate Conflict','Your roommate keeps breaking your study flow.','UNIVERSITY','RELATIONSHIP',NULL,NULL,NULL,18,24),
(8,'Internship Offer','A startup offers a demanding internship.','UNIVERSITY','CAREER',NULL,NULL,NULL,19,24),
(9,'Startup Opportunity','A friend asks you to co-found a student startup.','UNIVERSITY','RISK',60,40,NULL,19,25),
(10,'All-Nighter Season','Deadlines pile up and sleep disappears.','UNIVERSITY','HEALTH',NULL,NULL,30,18,30),
(11,'First Job Offer','You received your first full-time job offer.','EARLY_CAREER','CAREER',NULL,NULL,NULL,22,31),
(12,'Office Politics','A team conflict threatens your reputation.','EARLY_CAREER','CAREER',NULL,NULL,NULL,22,35),
(13,'Crypto Craze','A risky investment trend is exploding online.','EARLY_CAREER','MONEY',NULL,35,NULL,22,35),
(14,'Health Check Alert','A checkup reveals stress markers.','EARLY_CAREER','HEALTH',NULL,NULL,35,22,40),
(15,'Graduate School Offer','You are invited to a research masters program.','EARLY_CAREER','UNIVERSITY',80,NULL,NULL,22,32),
(16,'Family Commitment','Family responsibilities require your support.','ADULT_LIFE','RELATIONSHIP',NULL,NULL,NULL,28,60),
(17,'Leadership Promotion','You can step into a leadership role.','ADULT_LIFE','CAREER',NULL,NULL,NULL,28,65),
(18,'Community Initiative','Local residents want you to lead a social project.','ADULT_LIFE','LIFE',NULL,NULL,NULL,28,65),
(19,'Midlife Pivot','You feel called to change your path.','ADULT_LIFE','LIFE',NULL,NULL,NULL,30,65),
(20,'Legacy Decision','You are deciding what legacy to build.','ADULT_LIFE','LIFE',NULL,NULL,NULL,32,70);

INSERT INTO choices (event_id,text,money_delta,intelligence_delta,happiness_delta,health_delta,reputation_delta,risk_delta,relationships_delta,age_delta) VALUES
(1,'Study hard',0,15,-5,-5,0,-2,-2,1),(1,'Relax and hope',0,-10,10,2,-3,5,2,1),(1,'Cheat',0,5,0,-2,-20,20,-5,1),
(2,'Build a serious project',-5,12,-2,-1,8,1,2,1),(2,'Do minimum work',0,-4,4,1,-2,0,0,1),(2,'Skip it',0,-8,6,3,-5,0,-1,1),
(3,'Accept and teach well',15,6,-2,-1,6,0,3,1),(3,'Charge too much',25,0,0,0,-6,8,-4,1),(3,'Decline politely',0,0,2,0,1,0,2,1),
(4,'Train hard and compete',0,2,4,10,5,0,3,1),(4,'Bench and observe',0,0,1,2,0,0,1,1),(4,'Fake injury',0,0,0,0,-8,3,-3,1),
(5,'Take the scholarship test',10,10,-2,-1,5,1,0,1),(5,'Ignore it',0,-3,2,1,-2,0,0,1),(5,'Cheat with leaked answers',20,2,0,-2,-15,18,-6,1),
(6,'Join coding club',-3,8,3,-1,4,1,6,1),(6,'Join party club',-8,-1,10,-4,2,5,10,1),(6,'Join none',0,0,-2,2,-1,0,-2,1),
(7,'Talk and compromise',0,1,4,1,3,-2,8,1),(7,'Report them aggressively',0,0,-3,0,-4,4,-6,1),(7,'Move out suddenly',-15,0,2,-1,-1,3,-2,1),
(8,'Take internship',20,10,-4,-5,5,4,2,1),(8,'Negotiate lighter role',12,6,1,-2,3,1,1,1),(8,'Reject and focus studies',0,8,-1,1,0,-1,0,1),
(9,'Build startup nights/weekends',40,8,-6,-6,6,12,2,1),(9,'Plan slowly first',10,4,1,-1,2,3,1,1),(9,'Reject risky idea',0,-1,2,2,0,-8,0,1),
(10,'Keep pushing through',10,6,-6,-12,2,4,-4,1),(10,'Take recovery break',-8,-2,6,12,0,-3,4,1),(10,'Drop a class',0,-4,3,5,-1,-2,1,1),
(11,'Accept highest salary',30,2,-2,-3,2,2,-1,2),(11,'Pick growth role',15,8,0,-2,5,3,2,2),(11,'Take relaxed job',8,0,8,6,0,-4,4,2),
(12,'Handle diplomatically',5,2,0,0,8,-2,3,1),(12,'Play politics back',10,0,-3,-1,-6,8,-4,1),(12,'Avoid conflict',0,-1,2,1,-2,-1,0,1),
(13,'Invest carefully',20,2,1,0,1,5,0,1),(13,'Go all-in',70,0,5,-4,-6,20,-5,1),(13,'Stay out',0,0,0,1,0,-4,1,1),
(14,'Ignore warning signs',15,0,-4,-12,-1,4,-3,1),(14,'Start fitness routine',-10,0,4,12,3,-3,2,1),(14,'Seek therapy and support',-12,2,8,10,4,-4,6,1),
(15,'Enroll in grad school',-25,12,-2,-2,4,0,-1,2),(15,'Continue working',20,2,1,-1,2,1,0,2),(15,'Decline rudely',0,0,-2,0,-6,2,-2,2),
(16,'Support family financially',-20,0,2,-2,6,-1,12,2),(16,'Set boundaries kindly',0,1,5,2,3,-2,8,2),(16,'Avoid responsibility',15,0,-6,0,-8,3,-10,2),
(17,'Accept leadership role',35,4,-3,-3,10,5,2,2),(17,'Stay individual contributor',20,2,2,1,3,-2,1,2),(17,'Switch jobs impulsively',10,0,-1,-2,-5,8,-2,2),
(18,'Lead community project',-5,2,6,-1,12,1,10,2),(18,'Donate quietly',-15,0,3,0,5,0,2,2),(18,'Ignore request',0,0,-2,0,-4,0,-1,2),
(19,'Launch a new venture',50,6,-2,-5,4,12,0,2),(19,'Rebalance life priorities',-5,1,10,8,3,-4,8,2),(19,'Do nothing',0,0,-3,-2,-1,0,-2,2),
(20,'Mentor next generation',-10,4,8,2,10,-2,10,2),(20,'Chase one final gamble',80,0,-4,-6,-4,18,-6,2),(20,'Retire peacefully',5,0,7,6,4,-3,6,2);
