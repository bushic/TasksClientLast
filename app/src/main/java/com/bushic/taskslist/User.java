package com.bushic.taskslist;

public class User {

        private long id;
        private String login;
        private String password;
        private Boolean root;

        public User(){

        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Boolean getRoot() {
            return root;
        }

        public void setRoot(Boolean root) {
            this.root = root;
        }
}

