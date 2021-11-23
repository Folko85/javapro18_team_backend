INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'BLOCKED');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='jboner0@domainmarket.com'),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'WASBLOCKEDBY');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net'),
    (select person.id from person WHERE person.e_mail='cfurby1@webeden.co.uk')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'DEADLOCK');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net'),
    (select person.id from person WHERE person.e_mail='bjacobowits2@wsj.com')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'BLOCKED');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='jpeagram3@virginia.edu'),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'FRIEND');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='jboner0@domainmarket.com'),
    (select person.id from person WHERE person.e_mail='cfurby1@webeden.co.uk')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'WASBLOCKEDBY');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net'),
    (select person.id from person WHERE person.e_mail='gdomerc5@unesco.org')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'FRIEND');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kmattussevich6@alibaba.com'),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'FRIEND');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net'),
    (select person.id from person WHERE person.e_mail='ipinar7@wisc.edu')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'REQUEST');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='goshea8@apple.com'),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net')
);
INSERT INTO friendship_status
(`time`, code)
VALUES('2021-11-22 23:34:13', 'REQUEST');
INSERT INTO friendship
(status_id, src_person_id, dst_person_id)
VALUES(
    (select MAX(id) from friendship_status),
    (select person.id from person WHERE person.e_mail='kburgan4@comcast.net'),
    (select person.id from person WHERE person.e_mail='sdeavin9@booking.com')
);