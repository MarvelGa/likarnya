TASK: ЛІКАРНЯ
Адміністратору системи доступний список Лікарів за категоріями (педіатр, травматолог,
хірург, ...) і список Пацієнтів. Реалізувати можливість сортування:
1) пацієнтів:
- за алфавітом;
- за датою народження;
2) лікарів:
- за алфавітом;
- за категорією;
- за кількістю пацієнтів.
  Адміністратор реєструє в системі пацієнтів і лікарів і призначає пацієнтові лікаря.
  Лікар визначає діагноз, робить призначення пацієнту (процедури, ліки, операції), які
  фіксуються в Лікарняній картці. Призначення може виконати Медсестра (процедури, ліки) або
  Лікар (будь-яке призначення).
  Пацієнт може бути виписаний з лікарні, при цьому фіксується остаточний діагноз.
  (Опціонально: реалізувати можливість збереження/експорту документа з інформацією про
  виписку пацієнта).
  
To Run the application:
1) Set your password and login for Mysql BD in the application.properties and another settings
2) The "likarnya2021177" will be automatically created when you run the application.
3) You should insert initial data for "likarnya202117" DB, the data.sql in located in the root
of the app in "db" folder.
4) Enter as Admin by email - adminTest@gmail.com and password "TTTttt555";
5) Go to http://localhost:8081/likarnya/login
  


