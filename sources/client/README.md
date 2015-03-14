# Инструкция по разворачиванию

В принципе, все описано ниже. И все же...

## Дано

* Машина с установленной IDE [WebStorm](https://www.jetbrains.com/webstorm/)
* Установлен [Git](http://git-scm.com/)
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
 
## Работа с клиентским проектом 
 
* Открываем каталог sources\branches\dev\client\ в *WebStorm*. Возможно автоматически запустится процесс индексации проекта (внизу
у IDE будет надпись Indexing...), лучше дождаться завершения, иначе будет притормаживать. 
* Открываем меню View -> Tool Windows -> Terminal. Это обычная командная строка. 
* Вводим команду `ember install`
* Вводим команду `ember build --watch --output-path=../server/public`. Эта команда собирает клиентский проект и помещает 
получившиеся файлы в серверный каталог public. Более того, благодаря флагу --watch, каждый раз при изменении любого 
файла в каталоге client/app эти изменения будут копироваться в server/public. Следует учитывать, что это слежение за 
файлами работает только до закрытия терминальной сессии (консоли) или нажатия Ctrl+C в консоли.
* Теперь нужно запустить сервер. Для этого в каталоге server нужно выполнить команду командной строки `sbt run`. 
Сервер будет доступен по адресу http://localhost:9000. При этом в браузере откроется страница server/public/index.html, т.е.
наше клиентское приложение. Чтобы остановить сервер нужно нажать Ctrl+D в консоли.
* После этого можно менять файлы с client/app, и все изменения будут автоматом копироваться в server/public. В браузере 
они будут доступны после перезагрузки страницы. Сервер при этом перезапускать не нужно.

## ECMAScript 6

При первом открытии какого-нибудь js-файла (например, app.js) из проекта WebStorm подчеркнет красным строки типа `import Ember from 'ember';`. Это происходит потому, что используется синтаксис ECMAScript 6, который еще не поддерживается в браузерах (но все к  этому идет). Ember-cli в режиме стартованного ember-сервера сам переведет ECMAScript 6 в ECMAScript 5 незаметно для нас. Для информации, результат можно посмотреть в каталоге dist. Однако, чтобы красные красные подчеркивания не досаждали ставим курсор на такую строку и нажимаем Alt+Enter (в WebStorm вообще так - не знаешь, что делать, жми Alt+Enter). IDE предложит нам включить синтаксис ECMAScript 6, соглашаемся. Так же IDE предложит нам включить File Watcher, который будет перводить файлы с ECMAScript 6 синтаксисом в файлы ECMAScript 5. Это фича самого WebStorm. Нам это не нужно, т.к. это делает Ember-cli, следовательно отказываемся.

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

