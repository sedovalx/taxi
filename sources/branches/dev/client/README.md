# Инструкция по разворачиванию

В принципе, все описано ниже. И все же...

## Дано

* Машина с установленной IDE [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/download/)
* Доступ в интернет

##Предустановки

* Установить [Node.js](http://nodejs.org/) (with NPM). 
При установке в переменную окружения PATH должен добавиться путь к исполняемым скриптам node и npm. Далее мы будем часто 
пользоваться npm - это менеджер пакетов для NodeJS. 
* Установить npm-пакет [Bower](http://bower.io/). 
Для этого в командной строке пишем `npm install -g bower`. Это менеджер пакетов для браузерного приложения, работает на 
базе NodeJS. С его помощью мы будем добавлять разные плюшки к приложению.
* Установить npm-пакет [Ember CLI](http://www.ember-cli.com/).
Для этого выполняем `npm install -g ember-cli`. Это интерфейс командной строки, который поможет нам в разработке приложения.
* Установить [Git](http://git-scm.com/)

## GitHub

* В IDEA нужно настроить использование Git. Идем в Settings -> Version control -> Git. Убеждаемся, что там указан верный 
путь на исполняемый файл Git'а. Можно проверить, нажав кнопку Test.
* Если еще не зарегистрированы на GitHub, то регистрируемся. 
* В IDEA нужно указать логин и пароль для GitHub, чтобы не вводить их каждый раз. Для этого идем в Settings -> Version Control -> GitHub.
* Клонируем репозиторий из GitHub. Для этого идем в меню VCS -> Checkout from version control -> GitHub, указываем [адрес 
репозитория](https://github.com/sedovalx/taxi.git), родительский каталог, в котором создастся локальный репозиторий, и 
имя каталога с репозиторием.
 
## Работа с клиентским проектом 
 
* Открываем каталог sources\branches\dev\client\ в IDEA. Возможно автоматически запустится процесс индексации проекта (внизу
у IDE будет надпись Indexing...), лучше дождаться завершения, иначе будет притормаживать. 
* Открываем меню View -> Tool Windows -> Terminal. Это обычная командная строка. Вводим там команду `ember server`. 
После этого клиент запустится в браузере по адресу http://localhost:4200. 
* Можно менять файлы проекта в каталоге app. При сохранении страница в браузере сама обновится, т.е. сервер после изменения
файлов перезапускать не нужно.
* На какой-то стадии разработки нам нужно будет обращаться к контроллерам нашего Scala-сервера за данными из БД. Тогда 
помимо запуска клиента нужно будет запускать и Scala-сервер. Для этого в каталоге с сервером нужно выполнить команду
командной строки `sbt run`. Сервер будет доступен по адресу http://localhost:9000.

## Установка зависимостей

Часто в проекте требуется какая-нибудь сторонняя библиотека типа jQuery, Bootstrap, momentjs и пр. Большинство из них
распространяется с помощью bower-пакетов. Чтобы найти нужный пакет часто достаточно написать в google 
`bower предполагаемое_имя_пакета`. Обычно на страницах библиотек есть прямо строка с командой для bower, что-то вроде
`bower install bootstrap --save`. После выполнения этой команды в каталоге клиентского проекта bower ищет нужный пакет
по имени и скачивает его в каталог bower_components. У нас Ember-проект, он устроен немного иначе. Для установки 
bower-зависимости нужно выполнить команду `ember install:bower bootstrap`. После чего в файле Brocfile.js [прописать
установленную зависимость](http://www.ember-cli.com/#managing-dependencies). 


# Client

This README outlines the details of collaborating on this Ember application.
A short introduction of this app could easily go here.

## Prerequisites

You will need the following things properly installed on your computer.

* [Git](http://git-scm.com/)
* [Node.js](http://nodejs.org/) (with NPM)
* [Bower](http://bower.io/)
* [Ember CLI](http://www.ember-cli.com/)
* [PhantomJS](http://phantomjs.org/)

## Installation

* `git clone <repository-url>` this repository
* change into the new directory
* `npm install`
* `bower install`

## Running / Development

* `ember server`
* Visit your app at [http://localhost:4200](http://localhost:4200).

### Code Generators

Make use of the many generators for code, try `ember help generate` for more details

### Running Tests

* `ember test`
* `ember test --server`

### Building

* `ember build` (development)
* `ember build --environment production` (production)

### Deploying

Specify what it takes to deploy your app.

## Further Reading / Useful Links

* [ember.js](http://emberjs.com/)
* [ember-cli](http://www.ember-cli.com/)
* Development Browser Extensions
  * [ember inspector for chrome](https://chrome.google.com/webstore/detail/ember-inspector/bmdblncegkenkacieihfhpjfppoconhi)
  * [ember inspector for firefox](https://addons.mozilla.org/en-US/firefox/addon/ember-inspector/)

