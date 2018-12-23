package com.lintend.forum;

 public class DataModule {

     String name;
     String Question;
     String Time;
     String id;
     String vote_count;
     String answer_id;
     public String getAnswer_id() {
         return answer_id;
     }

     public void setAnswer_id(String answer_id) {
         this.answer_id = answer_id;
     }



     public String getVote_count() {
         return vote_count;
     }

     public void setVote_count(String vote_count) {
         this.vote_count = vote_count;
     }



     public String getAnswers() {
         return answers;
     }

     public void setAnswers(String answers) {
         this.answers = answers;
     }

     String answers;

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }



     public String getQuestion() {
         return Question;
     }

     public void setQuestion(String question) {
         Question = question;
     }

     public String getTime() {
         return Time;
     }

     public void setTime(String time) {
         Time = time;
     }



     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }



     public DataModule() {
     }

     public DataModule(String name, String question) {
         this.name = name;
         Question = question;
     }
 }
