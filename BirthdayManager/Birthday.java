package birthdaymanager;

import java.time.LocalDate;

public class Birthday {

    private int id;
    private String name;
    private LocalDate birthday;

    public Birthday(int id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    // জন্মদিন Bangla format এ দেখানোর জন্য
    public String getBirthdayBangla() {
        String[] months = {
                "জানুয়ারী","ফেব্রুয়ারী","মার্চ","এপ্রিল",
                "মে","জুন","জুলাই","আগস্ট",
                "সেপ্টেম্বর","অক্টোবর","নভেম্বর","ডিসেম্বর"
        };

        return birthday.getDayOfMonth() + " "
                + months[birthday.getMonthValue() - 1]
                + ", " + birthday.getYear();
    }
}
