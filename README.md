<h1>Poll Master</h1>
<div>
This is a repository for a simple polling system called Poll Master. It was made by Jerome Bustarga and Colton Crowl.

It was developed on springboot using java and HTML.

This code can be locally run on a users own computer if they wanted.
</div>

<h1>File System</h1>
<div>
The file system includes the following files:
<br>A Security Configure File.
<br>Multiple Controller Files including:
  <ul>
  <li>An Authority Controller.</li>
  <li>A Global Exception Handler.</li>
  <li>A Home Controller.</li>
  <li>An Index Controller.</li>
  <li>And a Poll Controller.</li>
  </ul>
A DTO for Poll and Question Creation.
<br>Multiple Model Files including:
  <ul>
    <li>Option</li>
    <li>Poll</li>
    <li>Question</li>
    <li>Role</li>
    <li>User</li>
  </ul>
Two Repository files for Poll and User Data.
<br> And three Service Files for Poll and User Service and Poll Implementation.
<h1>Dependencies</h1>
The following Dependencies are required
  <ul>
    <li>spring-boot-start-thymeleaf</li>
    <li>spring-boot-starter-web</li>
    <li>spring-boot-devtools</li>
    <li>spring-boot-start-test</li>
    <li>spring-boot-web-service</li>
    <li>spring-boot-reactive-web</li>
  </ul>
<h1>Application Instruction</h1>
In order to run the application either run the PollingappApplication.java code or using ./mvnw spring-boot:run in the terminal.
<br>Then go to your browser and navigate to http://localhost:8080/.
<br>From there register for an account and login.
<br>After logging in you will be taken to the Home Page where you can then either create a poll, or enter a code for a Poll you would like to take.
<br>If you create a Poll, you will be taken to the Create A Poll page where you can give the poll a Name, and add questions and answers to the poll.
<br>After creating the Poll you will be taken to Poll Management page where you can see the poll you made and review your work. Should any mistakes have been made or you simply wish to add a new question you can change the poll with the edit poll button. Otherwise the poll can be shared using the code given in the details.
<br>If you wish to see all the polls you have made, the Create a Poll page has a My Polls button where you can see all of the polls you have made.
<br>If you wish to take someone elses poll, enter the poll code on the home page and fill out the form. At the end you will be able to see the answers that you put in the review.
</div>
