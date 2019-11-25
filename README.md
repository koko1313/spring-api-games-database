# Games database with API

## Задание

##### Да се направи уеб-приложение, което да надгражда приложението направено по задание „ЗАДАНИЕ ПРАКТИКУМ МАГИСТРИ 2019/2020“. Новата версия на приложението трябва да изпълнява следните задачи:

-------------------------------------------------------------------------------

1. Приложението да използва релационна база от данни, която притежава три релационно свързани таблици (при ваше желание може и повече). Вашата ДБ трябва да спазва добрите практики и да е в трета нормална форма;

2. Трябва да изградите REST end point(s), който (които) да обслужват базови заявки към вашето приложение, реализиращи CRUD операции с базата данни;

3. Трябва да разширите вашето приложение с допълнителни страници, които да имат подходящ потребителски интерфейс, подпомагащ CRUD операциите;

4. Нека вашето приложение да има визуализация на данни от ДБ, чрез използване на филтър (някакъв вид търсене) по един и по два критерия;

-------------------------------------------------------------------------------

*Темата на приложението може да бъде директно продължение на проекта от Практикум или да бъде ново приложение, но задължително трябва да изпълнява и условията от предходното задание (Задание Практикум Магистри 2019/2020).*

*За изпълнение на проекта използвайте технологиите преподавани през триместъра по Практикум и трите базови дисциплини.*

*Проектът е предвиден за самостоятелна работа, съответно всички предаващи проекти ще бъдат изпитвани относно функционалностите и работата на проекта. Проектът се защитава на посочените официални изпитни дати.*


===============================================================================


## Genres

1. **Path:** */genre/all*  
	**Method:** GET  
	**Returns:** **all the genres** with **HttpStatus 200**. Example:  
		```json
		[
			{
				"id": 1,
				"name": "Horror"
			},
		]
		```

2. **Path:** */genre/insert*  
	**Method:** POST  
	**Parameters:**  
		- **name** *- the name of the genre*  
	**Returns:**  
		1. The **inserted genre** with **HttpStatus 201** *- if the genre is inserted successfully*
			```json
			{
				"id": 1,
				"name": "Horror"
			}
			```
		2. **Null** with **HttpStatus 400** *- if the genre already exist*
		3. **Null** with **HttpStatus 404** *- if the genre is not inserted successfully*
		
3. **Path:** */genre/update*  
	**Method:** PUT  
	**Parameters:**  
		- **id** *- the id of the genre that will be updated*  
		- **name** *- the name of the genre*  
	**Returns:**  
		1. The **updated genre** with **HttpStatus 200** *- if the genre is updated successfully*
			```json
			{
				"id": 1,
				"name": "Horror"
			}
			```
		2. **Null** with **HttpStatus 404** *- if the genre does not exist*
		3. **Null** with **HttpStatus 400** *- if the genre is not updated successfully*
		
4. **Path:** */genre/delete*  
	**Method:** DELETE  
	**Parameters:**  
		- **id** *- the id of the genre that will be deleted*   
	**Returns:**  
		1. **True** with **HttpStatus 200** *- if the genre is deleted successfully**
		2. **False** with **HttpStatus 404** *- if the genre is not found*