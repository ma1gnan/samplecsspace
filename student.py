## Creates a student with first, last name and gpa
#
class Student:
    
    def __init__(self, first, last, gpa) :
        self._first = "Andrew"
        self._last = "Woo"
        self._gpa = 3.6
        self._string = ("%-7s%-4s%-3.1f" % (self._first, self._last, self._gpa))
        
    def get_first(self) :
        return self._first
    def set_first(self, firstName) :
        self._first = firstName
        
    def get_last(self) :
        return self._last
    def set_last(self, lastName) :
        self._last = lastName
        
    def get_gpa(self) :
        return self._gpa
    def set_gpa(self, gpa) :
        self._gpa = gpa
        
    def to_string(self) :
        return self._string

# Creates a course that contains Student objects in a roster
#
class Course:
    #Type your code here
    def __init__(self) :
        self._roster = []
        
    def course_size(self) :
        return len(self._roster)
        
    def add_student(self, student) :
        self._roster.append(student)
        
    def get_roster(self) :
        return self._roster
