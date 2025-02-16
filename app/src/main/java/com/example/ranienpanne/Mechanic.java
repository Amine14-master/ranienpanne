package com.example.ranienpanne;

        public class Mechanic {
            private String username;
            private String phone;
            private String position;
            private String description;

            public Mechanic() {
                // Default constructor required for calls to DataSnapshot.getValue(Mechanic.class)
            }

            public Mechanic(String username, String phone, String position, String description) {
                this.username = username;
                this.phone = phone;
                this.position = position;
                this.description = description;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
