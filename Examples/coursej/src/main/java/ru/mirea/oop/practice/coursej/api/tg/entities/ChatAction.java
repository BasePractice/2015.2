package ru.mirea.oop.practice.coursej.api.tg.entities;

public enum ChatAction {
    TYPING {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    UPLOAD_PHOTO {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    UPLOAD_VIDEO {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    UPLOAD_AUDIO {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    UPLOAD_DOCUMENT {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    },
    FIND_LOCATION {
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
