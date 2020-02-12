# KNP4J
JUMAN(JUMAN++)、KNPのプロセスをJavaから実行して結果を取得するライブラリです。  

# Maven/Gradle
https://search.maven.org/artifact/dev.natsuume/knp4j

* Maven
```
<dependency>
  <groupId>dev.natsuume.knp4j</groupId>
  <artifactId>knp4j</artifactId>
  <version>1.1.3</version>
</dependency>
```

* Gradle
```
implementation 'dev.natsuume.knp4j:knp4j:1.1.3'
```

# 使い方

下記のように使用することができます。  
各種オプションのうちParserの設定は必須です。  
また、それ以外のオプションについては下記例の内容がデフォルトで使用されます。
```
//KNPWrapperを作成するためのBuilder
ResultParser<KnpResult> knpResultParser = new KnpResultParser();
KnpWrapperBuilder<KnpResult> knpWrapperBuilder = new KnpWrapperBuilder<>();
KnpWrapper<KnpResult> wrapper = knpWrapperBuilder
    .setJumanCommand(List.of("bash", "-c", "jumanpp")) //Jumanの実行コマンド
    .setKnpCommand(List.of("bash", "-c", "knp -tab -print-num -anaphora")) //KNPの実行コマンド(現在は「-tab」「-print-num」「-anaphora」オプション必須)
    .setJumanMaxNum(1) //同時に起動するJumanの最大プロセス数
    .setJumanStartNum(1) //初期化時に起動するJumanのプロセス数
    .setKnpMaxNum(1) //同時に起動するKNPの最大プロセス数
    .setKnpStartNum(1) //初期化時に起動するKNPのプロセス数
    .setRetryNum(0) //結果の取得に失敗した場合にリトライする回数
    .setResultParser(knpResultParser) //出力結果のList<String>を任意のクラスに変換するParserを設定する
    .start();
var texts = List.of(
    "テストテキスト1です",
    "テストテキスト2です",
    "テストテキスト3です"
);
texts.parallelStream().map(wrapper::analyze)
    .flatMap(List::stream)
    .map(KnpResult::getSurfaceForm)
    .forEach(System.out::println);
```

現在KNPの解析結果ParserとしてKnpResultParserのみ実装されています。  

KnpResultは下記メソッドを持ちます。
```
  /**
   * 文節のリストを返す.
   *
   * @return 文節の一覧
   */
  List<KnpClause> getClauses();

  /**
   * 形態素のリストを返す.
   *
   * @return 形態素の一覧
   */
  List<KnpMorpheme> getMorphemes();

  /**
   * 基本句のリストを返す.
   *
   * @return 基本句の一覧
   */
  List<KnpPhrase> getPhrases();

  /**
   * 文末の文節を返す. 要素が存在しないときはnullを返す.
   *
   * @return 文末のKnpClause
   */
  KnpClause getRootNode();

  /**
   * KNPの出力結果の文字列リストを返す.
   *
   * @return 出力結果
   */
  List<String> getRawResultData();

  /**
   * 入力された表層文字列を返す.
   *
   * @return 入力文字列
   */
  String getSurfaceForm();

  /**
   * 解析スコアを返す.
   *
   * @return 解析スコア
   */
  double getScore();

  /**
   * 正常な解析結果かどうかを返す.
   *
   * @return 正常な解析結果かどうか
   */
  boolean isValid();
```

なお現在、JUMAN, KNPについてサーバモードでの起動は対応していません。

# LICENSE

このソフトウェアは下記を使用しています。  
* [Google Guava Libraries](https://github.com/google/guava/)
* [vavr](https://github.com/vavr-io/vavr)  

これらのライセンス条項は[LICENSE](LICENSE)にあります。

このソフトウェアはソースコードのFormatに[checkstyle](https://github.com/checkstyle/checkstyle)の[google_checks.xml](config/google_checks.xml)を利用しています。  
上記ファイルのライセンス条項は[checkstyle_LICENSE](config/checkstyle_LICENSE)にあります。

# 更新履歴
* 2020/02/12: **ver.1.1.3 update**
  * MajorPartOfSpeechに助詞が定義されていなかったため、助詞を表す`POSTPOSITIONAL_PARTICLE`を追加
* 2020/02/10: **ver.1.1.2 update**
  * 入力文に`#`が含まれる場合にKnpResultBuilderがparsingに失敗する問題を修正
* 2020/02/08: **ver.1.1.1 update**
  * KnpWrapperBuilderのデフォルトKnpコマンドから`-anaphora`オプションを削除
* 2020/02/05: **ver.1.1.0 update**
  * KnpClause, KnpPhraseが任意のfeatureを含んでいるかの判定を行うメソッドを追加
  * 全体のコードフォーマットを統一
  * 一部javadocコメントの追加・修正
  * 使用されていないprivateなメソッドやフィールドを削除
* 2019/12/17: **ver.1.0.0 release**