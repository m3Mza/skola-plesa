# Setup Instructions for Edit Course Feature

## 1. Run the Stored Procedure SQL

Execute the following SQL in your MySQL database:

```bash
mysql -u root -p skola_plesa < sp_izmeni_cas.sql
```

Or open MySQL and run:

```sql
source /Applications/MAMP/htdocs/skola-plesa-java/sp_izmeni_cas.sql
```

Or manually execute the content of `sp_izmeni_cas.sql` in phpMyAdmin or MySQL Workbench.

## 2. Restart the Application

The application has been rebuilt with the following fixes:

### Fixed Issues:

1. **Izmeni Button Now Opens Edit Modal** ✅
   - Fixed Thymeleaf syntax: `th:data-bs-target="${'#editModal' + cas.id}"`
   - Fixed modal ID: `th:id="${'editModal' + cas.id}"`
   - Each course now has a unique modal with proper ID matching

2. **All Courses Display on Instructor's Raspored Page** ✅
   - The service already returns all courses for instructors
   - The HTML template loops through all courses with `th:each="cas : ${rasporedViewModel.rasporedi}"`
   - Debug logging added to verify all courses are loaded

3. **Edit Functionality Now Uses Stored Procedure** ✅
   - Created `sp_izmeni_cas` stored procedure
   - Updated DAO, Repository, Service, and Controller layers
   - Includes validation and conflict checking

4. **Flash Messages Fixed** ✅
   - Changed from `success`/`error` to `uspeh`/`greska` to match controller

## 3. Start the Application

```bash
cd /Applications/MAMP/htdocs/skola-plesa-java
java -jar target/skola-plesa-java-0.0.1-SNAPSHOT.jar
```

## Testing

1. Login as instructor (bane.instruktor@skola.com / instruktor123)
2. Go to Raspored page
3. All created courses should be visible
4. Click "Izmeni" button - modal should open
5. Edit course details and save
6. Click "Obriši" to delete a course
7. Logout dropdown should work properly

## Debug Output

The application now logs detailed information:
- Number of courses loaded from database
- Course details (ID, type, date, location)
- Whether user is instructor or student
- Filtering logic for students
