select*from student;

select*from student where age > 18 and age <= 20;

select*from student where age between 19 and 20;

select name from student;

select*from student where name like '%О%' or name like '%о%';

select*from student where age < student.id;

select * from student order by age;