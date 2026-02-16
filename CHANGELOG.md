# Changelog

## [1.13.0](https://github.com/GnomeShift/TiSk/compare/1.12.6...1.13.0) (2026-02-16)


### 🚀 Features

* add rich text editor ([#40](https://github.com/GnomeShift/TiSk/issues/40)) ([90d8cce](https://github.com/GnomeShift/TiSk/commit/90d8ccee12186e00653e1684b845b05a932955a1))
* **front:** add errors translation ([25b0e1c](https://github.com/GnomeShift/TiSk/commit/25b0e1cd699474c1590cd74794e486243926cde2))
* **front:** add rich text editor ([804f5a5](https://github.com/GnomeShift/TiSk/commit/804f5a5b906ca7be561b2369cfe0a168f487a4e5))


### 🐛 Bug fixes

* **back:** `DIVE` ([953566b](https://github.com/GnomeShift/TiSk/commit/953566b53b862393243b2546c941e2ee159bb817))
* **back:** `NPE` ([e5ec2c3](https://github.com/GnomeShift/TiSk/commit/e5ec2c32c7a4fcb7e630e3db7e4d2d59cce1f16e))
* **back:** confidential information disclosure ([958a148](https://github.com/GnomeShift/TiSk/commit/958a148dd60f362608e2990b0d82a7480d849460))
* **back:** infinite token refreshing ([11ba4d2](https://github.com/GnomeShift/TiSk/commit/11ba4d2fd8d928c4605ba432f8cad861df2ab4c6))
* **back:** privilege escalation ([2cda047](https://github.com/GnomeShift/TiSk/commit/2cda047db989a061ff77af54039c16db8306fdd2))
* **back:** security ([#39](https://github.com/GnomeShift/TiSk/issues/39)) ([b751f37](https://github.com/GnomeShift/TiSk/commit/b751f377ce9c41cc34f471739e134562b684b6b2))
* **back:** stored XSS ([ff1ac70](https://github.com/GnomeShift/TiSk/commit/ff1ac70c1d077d58ce42adade2f8fcafd33a7784))
* **back:** typos ([4b85908](https://github.com/GnomeShift/TiSk/commit/4b8590807568d0516c16b2d82990b8781c8d4a3b))
* **back:** user can't edit ticket ([7fcdd9b](https://github.com/GnomeShift/TiSk/commit/7fcdd9b68924b7ff52c561b1c9eb46afb5ff0bee))
* **front:** can't remove table after row deletion ([09a7935](https://github.com/GnomeShift/TiSk/commit/09a79352ac2c7e8f12354edfc142c5a55326c360))
* **front:** edit modal closes on error ([5b010d7](https://github.com/GnomeShift/TiSk/commit/5b010d7a20faa827ff63de9751bd86ee4a852475))
* **front:** improve code quality ([0ad01d3](https://github.com/GnomeShift/TiSk/commit/0ad01d3a67f1bcb5e144d3ae4b3d182a5fe302f8))
* **front:** incomplete multi-character sanitization ([4c7eeaa](https://github.com/GnomeShift/TiSk/commit/4c7eeaad13db96ce1c7528f00cf926aaa42f458f))
* **front:** incorrect auth endpoints interception ([620ef9c](https://github.com/GnomeShift/TiSk/commit/620ef9cd2aee9baeb65ffecfb35ccce1de432109))
* **front:** infinite loop ([3a50ff9](https://github.com/GnomeShift/TiSk/commit/3a50ff9f05e85fb7e31ca45f335fbbec5830f6cf))
* **front:** long unspaced text breaks page layout ([cdf69d1](https://github.com/GnomeShift/TiSk/commit/cdf69d1f3448743f7ed534564fca17257fab7c71))
* **front:** memory leak ([df05025](https://github.com/GnomeShift/TiSk/commit/df05025e7e9beabd303e2a9e72aca5180562097c))
* **front:** missing async error handling ([2fcb010](https://github.com/GnomeShift/TiSk/commit/2fcb01029ac359697a4e4fa6613641dafd8f87e5))
* **front:** missing dark green color ([6737147](https://github.com/GnomeShift/TiSk/commit/6737147ac724e3a0615019a5919b28f8a16ad696))
* **front:** missing permission to view all tickets ([74162e2](https://github.com/GnomeShift/TiSk/commit/74162e2f445c612cc391c62550daf7858d56c423))
* **front:** pages ([#43](https://github.com/GnomeShift/TiSk/issues/43)) ([45ce244](https://github.com/GnomeShift/TiSk/commit/45ce244db07c84f02cdfd2e65cb49fc39a0b84d0))
* **front:** state sync ([7784ba4](https://github.com/GnomeShift/TiSk/commit/7784ba404dabc7cd0fa0fb20470b44a18be8a2e6))
* **front:** unexpected logout on network error ([77f5e0a](https://github.com/GnomeShift/TiSk/commit/77f5e0af8b2bce44296b5e778ddc194571af47a2))
* **front:** XSS ([45966cf](https://github.com/GnomeShift/TiSk/commit/45966cf8f182cc7cad030678ea9b0007bb8ada52))
* security ([#44](https://github.com/GnomeShift/TiSk/issues/44)) ([cba26c9](https://github.com/GnomeShift/TiSk/commit/cba26c9362bbe9eb3a2cc0795deb01c0bc2b6191))


### ⚡ Performance

* **front:** optimize `AuthContext` ([0ee0c68](https://github.com/GnomeShift/TiSk/commit/0ee0c6876c24da2c260fa92121469fb182824465))


### ♻️ Refactoring

* **back:** improve statistic queries ([e6aac80](https://github.com/GnomeShift/TiSk/commit/e6aac8038805846f339f5d82e588515f68cc0017))
* **back:** make the first user an admin ([75b5d59](https://github.com/GnomeShift/TiSk/commit/75b5d59877dcaafed92066ba30cc2cfe27e2c3b5))
* **back:** replace JWT subject from email to user id ([49480f6](https://github.com/GnomeShift/TiSk/commit/49480f648ace7ff8d79fff6df5537f1a1d6781ad))
* **front:** improve char counter in `rich-text-editor` ([9b96c54](https://github.com/GnomeShift/TiSk/commit/9b96c54e8eb66ebba293f08fbd79fa9e276a8a6f))
* **front:** improve code quality ([04515cc](https://github.com/GnomeShift/TiSk/commit/04515cccf804fa358049bdff5ad297ee15ea28e0))
* **front:** improve validation ([d48e240](https://github.com/GnomeShift/TiSk/commit/d48e240160f8609a3024841e9220051ee616788c))
* **front:** remove hover effect on non-interactive elements ([a8f779c](https://github.com/GnomeShift/TiSk/commit/a8f779c666f6168d617d4bc62fffd1345082785c))
* **front:** remove page refresh on statistics period change ([fbc9f42](https://github.com/GnomeShift/TiSk/commit/fbc9f42bb8895871e08bc5eba28765ddff3d7484))
* **front:** show registration datetime ([50ccd4a](https://github.com/GnomeShift/TiSk/commit/50ccd4a3f7f7698f4e88bc43c6d00296fa97377f))
* **front:** slow down `Collapsible` animation ([5cb1e0f](https://github.com/GnomeShift/TiSk/commit/5cb1e0fc48ef9b8142848bb15a5b890e80d23b91))
* **front:** ui rework ([fa4ff78](https://github.com/GnomeShift/TiSk/commit/fa4ff78f33f1222383bf2a3229f5117c8b09f139))
* **front:** ui rework ([#38](https://github.com/GnomeShift/TiSk/issues/38)) ([2662f28](https://github.com/GnomeShift/TiSk/commit/2662f288e859a2f6cbf304656327b8dbcaab4908))
* increase ticket description length to `10 000` ([2539b43](https://github.com/GnomeShift/TiSk/commit/2539b436bc9a56eb9ca62f6248ebce1901d6ff75))


### 📚 Documentation

* update `README` ([e14015e](https://github.com/GnomeShift/TiSk/commit/e14015e8a68358b71bfc12bda4e1a3315723e217))
* update `README` ([#42](https://github.com/GnomeShift/TiSk/issues/42)) ([585e88d](https://github.com/GnomeShift/TiSk/commit/585e88d9864b49372a6ddeda9316ebdff17ccdbe))


### 🧪 Tests

* **back:** update tests ([56d8eb6](https://github.com/GnomeShift/TiSk/commit/56d8eb6471c45d5caf342bfaa872ccf8d62e3130))
* **back:** update tests ([f6fcefd](https://github.com/GnomeShift/TiSk/commit/f6fcefd68232359381881060fa8d1e27dbc33a88))
* update tests ([c10206f](https://github.com/GnomeShift/TiSk/commit/c10206f703956ce95fd00e3d11975996c8d7f97b))


### 🧹 Chores

* **deps:** bump axios ([8b8f16f](https://github.com/GnomeShift/TiSk/commit/8b8f16fff0bb146f5cbf5e2a291bff8c945e1494))
* **deps:** bump axios from `1.12.2` to `1.13.5` ([#45](https://github.com/GnomeShift/TiSk/issues/45)) ([85417be](https://github.com/GnomeShift/TiSk/commit/85417bedd0304444532bb4bc0731e04533a74a34))
* **deps:** bump markdown-it from `14.1.0` to `14.1.1` ([a96ccb6](https://github.com/GnomeShift/TiSk/commit/a96ccb66b112f84cc362bc0fa3826596f949dab7))
* **deps:** bump markdown-it from `14.1.0` to `14.1.1` ([#48](https://github.com/GnomeShift/TiSk/issues/48)) ([3ca1b87](https://github.com/GnomeShift/TiSk/commit/3ca1b87cca0392ba472b05c2407fa07ebb05b23a))
* release ([#47](https://github.com/GnomeShift/TiSk/issues/47)) ([4f59859](https://github.com/GnomeShift/TiSk/commit/4f598590a6fb926818ebd5a30781e0e5371facff))


### 🖌️ Styles

* **front:** restore ticket status position in `TicketDetail` ([297a1ec](https://github.com/GnomeShift/TiSk/commit/297a1ec1a016b92df624ebf96ee7f1a6f4b7494f))


### 📦 Build

* update build files ([9f4b470](https://github.com/GnomeShift/TiSk/commit/9f4b4703cacf9e7704c4fd26932b41a3a583d970))
* update build files ([#41](https://github.com/GnomeShift/TiSk/issues/41)) ([50383e7](https://github.com/GnomeShift/TiSk/commit/50383e78ea95e56c9a45ce64a2a1d2814213fcdc))

## [1.12.6](https://github.com/GnomeShift/TiSk/compare/1.12.6...1.12.6) (2026-01-11)


### 🚀 Features

* add `.env` handling ([bfe1b2a](https://github.com/GnomeShift/TiSk/commit/bfe1b2a2a81c5ea427a65c43b25103d4a534bed1))
* add frontend ([08c3b05](https://github.com/GnomeShift/TiSk/commit/08c3b05752bc8dd57c1c8fcfb62710a6352efb0e))
* add frontend ([c5957ee](https://github.com/GnomeShift/TiSk/commit/c5957eed745d1cb8e1acceb9944f91a289241497))
* add statistics ([#26](https://github.com/GnomeShift/TiSk/issues/26)) ([3b74ea6](https://github.com/GnomeShift/TiSk/commit/3b74ea61dbaf34122983f29ffbacbb3a3c16c96a))
* add ticket priority ([e502275](https://github.com/GnomeShift/TiSk/commit/e5022752ba0faa0ca2891e0600aaf9bd8656b55b))
* add ticket priority ([72e7531](https://github.com/GnomeShift/TiSk/commit/72e7531574e4e5029a378bef9e3d5ad01215eaae))
* add users and security ([#10](https://github.com/GnomeShift/TiSk/issues/10)) ([a524d26](https://github.com/GnomeShift/TiSk/commit/a524d2660f66ef73a63e15715cfb76fafe8b3841))
* **back:** add demo data seeding ([fc97090](https://github.com/GnomeShift/TiSk/commit/fc97090910bbee8a8d2636891a9cc1d50e4e2fca))
* **back:** add demo data seeding ([#23](https://github.com/GnomeShift/TiSk/issues/23)) ([5d0160e](https://github.com/GnomeShift/TiSk/commit/5d0160e8c3e4ea33dc84bc57e12726938f146c48))
* **back:** add entity relations ([639e8af](https://github.com/GnomeShift/TiSk/commit/639e8af6b84d86e3096cd5ef88a1fd70e3497e05))
* **back:** add metrics ([5c3547c](https://github.com/GnomeShift/TiSk/commit/5c3547c9b4d28ec018a8482b07e325e3d3e5d47f))
* **back:** add metrics ([#28](https://github.com/GnomeShift/TiSk/issues/28)) ([300b2b6](https://github.com/GnomeShift/TiSk/commit/300b2b6cff4fb46656981907ee20c4276b67b69e))
* **back:** add statistics ([5835ba8](https://github.com/GnomeShift/TiSk/commit/5835ba880695f205fe1506abb2179e42d1bb9043))
* **back:** add users and `Spring Security` ([eb998d2](https://github.com/GnomeShift/TiSk/commit/eb998d2f19f80223fc8b9c873ca1e027cb6b0152))
* **back:** explicitly add `entityNotFoundException` handler ([8711774](https://github.com/GnomeShift/TiSk/commit/8711774678ba516f164d815b487d0318cabdd506))
* **front:** add API URL and app title changing ([6de7059](https://github.com/GnomeShift/TiSk/commit/6de705925501aee392a60248a08e747a6d01df97))
* **front:** add API URL and app title changing ([#21](https://github.com/GnomeShift/TiSk/issues/21)) ([46a72f5](https://github.com/GnomeShift/TiSk/commit/46a72f50b403e2c0e280618e09704c57a92c36c2))
* **front:** add notifications ([4d97f32](https://github.com/GnomeShift/TiSk/commit/4d97f32bd51f7accb4d9cb54d5d2bf501adda6e7))
* **front:** add notifications ([#15](https://github.com/GnomeShift/TiSk/issues/15)) ([2ae9966](https://github.com/GnomeShift/TiSk/commit/2ae996600bf67c2086bbbb8e0408dcad3dd29ab9))
* **front:** add statistics ([644862c](https://github.com/GnomeShift/TiSk/commit/644862cf300b8e985c50d7ffa09011f24a3a1202))
* **front:** add status and priority localization ([9fde669](https://github.com/GnomeShift/TiSk/commit/9fde66966b0c84a4be47c98ed80b6038895484a1))
* **front:** add ticket search, filter and pagination ([d741131](https://github.com/GnomeShift/TiSk/commit/d741131473e8fa01f19a7be5465c4c74a0fe6389))
* **front:** add ticket search, filter and pagination ([62cb9cc](https://github.com/GnomeShift/TiSk/commit/62cb9cc75dcc3343447b478c0dffb519701841eb))
* **front:** add user management ([c07ed57](https://github.com/GnomeShift/TiSk/commit/c07ed5768501ee6002fde3d5532c81725e858135))
* **front:** add user management ([#12](https://github.com/GnomeShift/TiSk/issues/12)) ([36bf790](https://github.com/GnomeShift/TiSk/commit/36bf790347e475dd883161a5d6111648d1958b6e))
* **front:** add users and authentication ([f4a482b](https://github.com/GnomeShift/TiSk/commit/f4a482bb7fab8002ed96224c6b4f3b0516cc7d01))


### 🐛 Bug fixes

* **back:** `@NotNull` and `@NotBlank` in `PATCH` method ([b3d9054](https://github.com/GnomeShift/TiSk/commit/b3d905491b9503b7fb7e7983463545269dc91082))
* **back:** `stackOverflowException` while creating ticket due to infinite recursion ([82d095e](https://github.com/GnomeShift/TiSk/commit/82d095edfc33f5b78617477ae91ce5e2805358f5))
* **back:** double saving upon registration ([255654b](https://github.com/GnomeShift/TiSk/commit/255654b7d78f4b871d097efd7a91395d16eaee5b))
* **back:** infinite serialization ([07174b7](https://github.com/GnomeShift/TiSk/commit/07174b7ceeff816763ddf2b73a9df46a385e18a8))
* **back:** missing default values ([0f265a6](https://github.com/GnomeShift/TiSk/commit/0f265a654d40a49dee0944c59348981297cf1372))
* **back:** missing default values ([c3d0f9c](https://github.com/GnomeShift/TiSk/commit/c3d0f9cc92a323190f9a0aa54171c802d53bca4f))
* **back:** missing ticket existence check before deletion ([1607e05](https://github.com/GnomeShift/TiSk/commit/1607e0545aa549b80e36dfaf2a27f79081f17304))
* **back:** no acceptable representation ([bd5dee5](https://github.com/GnomeShift/TiSk/commit/bd5dee5069b24f672e4a9dcda00b6792300a7452))
* **back:** rollback CORS to stable ([6ea0774](https://github.com/GnomeShift/TiSk/commit/6ea0774352b3a7dee62db929f50a95c1e318a06f))
* **back:** rollback CORS to stable ([#30](https://github.com/GnomeShift/TiSk/issues/30)) ([35b5e89](https://github.com/GnomeShift/TiSk/commit/35b5e898995b9dca67afc84848936aed92f828ce))
* **back:** set assignee as reporter if reporter is present ([f3a0b7c](https://github.com/GnomeShift/TiSk/commit/f3a0b7cb82b3b47ef6f20157fd073edd54612d86))
* **back:** user can't edit ticket ([e92172b](https://github.com/GnomeShift/TiSk/commit/e92172bb15bae4336cdea55e664cbb7958206caf))
* **back:** user can't edit ticket ([#18](https://github.com/GnomeShift/TiSk/issues/18)) ([b907311](https://github.com/GnomeShift/TiSk/commit/b907311315cd0fa52ed0a18f534bd09506f6b89e))
* **back:** username conflicts with email ([7c657ac](https://github.com/GnomeShift/TiSk/commit/7c657ac13aae412457632ab7b3ddf222d32ce097))
* **front:** `SUSPENDED` status isn't stylized ([393438e](https://github.com/GnomeShift/TiSk/commit/393438ebe7c07344d7a4da85f9b5815fa5fb07f9))
* **front:** buttons don't fit into ticket card ([d80a3a8](https://github.com/GnomeShift/TiSk/commit/d80a3a8b5430c0e9d151032edefb141983410655))
* **front:** incorrect date display ([2c099cd](https://github.com/GnomeShift/TiSk/commit/2c099cdd6aa8c49979037fc66b5833db35877072))
* **front:** incorrect date display ([d7c163f](https://github.com/GnomeShift/TiSk/commit/d7c163f01ddadc39a4c1aa9535327d73d65525cd))
* **front:** incorrect return type ([b245a36](https://github.com/GnomeShift/TiSk/commit/b245a36c08ee159aef14b0545471b5d5965d9030))
* **front:** incorrect sort order in `sortOrder` ([04f145d](https://github.com/GnomeShift/TiSk/commit/04f145d37bb02c41a8a428e7ae554ba40fdec623))
* **front:** insufficient `body` height ([1239f49](https://github.com/GnomeShift/TiSk/commit/1239f49d5f7a9bb944eed6f4fb123009c381ada6))
* **front:** insufficient `body` height ([#19](https://github.com/GnomeShift/TiSk/issues/19)) ([6a08b1d](https://github.com/GnomeShift/TiSk/commit/6a08b1da0e32e20c9fb6401e6cc53fb1ebe333f5))
* **front:** race condition on token refresh ([09dd3e8](https://github.com/GnomeShift/TiSk/commit/09dd3e8397917a02610e4ac3aa89a8b645743092))
* **front:** replace pagination simulation with client pagination ([9fde669](https://github.com/GnomeShift/TiSk/commit/9fde66966b0c84a4be47c98ed80b6038895484a1))
* **front:** state desync on logout ([acca3a3](https://github.com/GnomeShift/TiSk/commit/acca3a32cf9b8bef8449308f9d6d2ee71e6d3daa))
* **front:** ticket priority, user status and role aren't stylized ([ee0dd43](https://github.com/GnomeShift/TiSk/commit/ee0dd435001998e9ace9ae979236295255fb9998))
* **front:** ticket status display ([d880f09](https://github.com/GnomeShift/TiSk/commit/d880f0934d9a393124277d44bf02fb0bbd3a6075))
* **front:** user can change ticket status ([d151735](https://github.com/GnomeShift/TiSk/commit/d151735e74acffadcb5624a4d5b8f48a5079981f))
* **front:** users table doesn't fit into visibility scope ([04619e3](https://github.com/GnomeShift/TiSk/commit/04619e360deea23c2a49c36f10c71cd4575fcde9))
* incorrect `phoneNumber` blank string validation ([50b6c31](https://github.com/GnomeShift/TiSk/commit/50b6c31df334244116b5d8b933ee7f9b12314500))
* incorrect validation ([#14](https://github.com/GnomeShift/TiSk/issues/14)) ([64aad46](https://github.com/GnomeShift/TiSk/commit/64aad46c33c7936edfbd41fa9caa560387dcc1b6))


### ♻️ Refactoring

* **api:** change `updateTicket` method from `PUT` to `PATCH` ([ad9ee7a](https://github.com/GnomeShift/TiSk/commit/ad9ee7a5e144d122644541e0bc3f3c0696d951ed))
* **api:** change `updateTicket` method from `PUT` to `PATCH` ([82fb132](https://github.com/GnomeShift/TiSk/commit/82fb132ddfe819d8f5b2eca8e298c1db9a7cdd65))
* **back:** add `validationException` and `BadCredentialsException` handling ([98644ce](https://github.com/GnomeShift/TiSk/commit/98644cebd6e663af87a20c97552633be33a598e2))
* **back:** cache `signingKey` and `JwtParser` ([e82fbe0](https://github.com/GnomeShift/TiSk/commit/e82fbe0762bbef1e24f819adb152d4e39e6fa966))
* **back:** code cleanup ([f8b318b](https://github.com/GnomeShift/TiSk/commit/f8b318bcd7d56885173ba741c3c5a1fb423b00e6))
* **back:** improve code quality ([#11](https://github.com/GnomeShift/TiSk/issues/11)) ([84b262e](https://github.com/GnomeShift/TiSk/commit/84b262ed315e7b462148e806b00cd8fb3db667cc))
* **back:** improve code quality ([#9](https://github.com/GnomeShift/TiSk/issues/9)) ([3193c3c](https://github.com/GnomeShift/TiSk/commit/3193c3c4b2f6edb2c7fb09f25e7bccbde945ce6a))
* **back:** improve dotenv handling ([02b026f](https://github.com/GnomeShift/TiSk/commit/02b026f3e47f19d33694babfa89b9a3d2686f7b2))
* **back:** improve security configuration ([00ecfde](https://github.com/GnomeShift/TiSk/commit/00ecfdebaafbd54466557a9dcda19c16628bbcd0))
* **back:** improve security configuration ([#29](https://github.com/GnomeShift/TiSk/issues/29)) ([31b794d](https://github.com/GnomeShift/TiSk/commit/31b794d6f0af8a7dbe4b51ee2e89b27f77b49cc8))
* **back:** remove `@ConfigurationProperties` from `JwtProperties` ([c02af57](https://github.com/GnomeShift/TiSk/commit/c02af57f7e26daeb7915bd691d1562fc0d192b72))
* **back:** suppress `Builder` and `MapStruct` warnings ([eb205a0](https://github.com/GnomeShift/TiSk/commit/eb205a05f72a93dd63eba72f4ab9fc9f535d1cd3))
* **back:** update `AuthenticationException` error message ([9500a5d](https://github.com/GnomeShift/TiSk/commit/9500a5d791e9de6976fd42f13102d51b8e3fb122))
* **back:** update validation error messages ([a014624](https://github.com/GnomeShift/TiSk/commit/a0146249bd1bdcb17828aba796e42ae34155194b))
* change user menu style ([#22](https://github.com/GnomeShift/TiSk/issues/22)) ([189ce0e](https://github.com/GnomeShift/TiSk/commit/189ce0eb8c184409f1c4718f7607c052c48718a0))
* **dotenv:** remove quotes due to incorrect parsing ([ecc316e](https://github.com/GnomeShift/TiSk/commit/ecc316e5f391b41201612f358d0dc528214a434b))
* **entity:** change ticket description data type from `VARCHAR` to `TEXT` ([14da035](https://github.com/GnomeShift/TiSk/commit/14da03502692ac580250ae300402546748d69553))
* **entity:** change ticket description data type from `VARCHAR` to `TEXT` ([f7f4aae](https://github.com/GnomeShift/TiSk/commit/f7f4aae48f0ea1ec1bd9419c80d247e7436c85fd))
* **front:** add `useCallback` and `useMemo` ([b863eb3](https://github.com/GnomeShift/TiSk/commit/b863eb3c539e2072c886588e9a20173d571344a3))
* **front:** add check for `undefined` in `env.ts` ([87b7113](https://github.com/GnomeShift/TiSk/commit/87b71136cc97b1fb4db841a1af325485d654e1f2))
* **front:** add space after profile sections ([f970ee3](https://github.com/GnomeShift/TiSk/commit/f970ee31306c6506833c6d7a425aaa4c52f5b075))
* **front:** add space after user profile data ([fd78d33](https://github.com/GnomeShift/TiSk/commit/fd78d33dfcd584f39b8f518de074c19e87af4eb4))
* **front:** change assignee modal window style ([395f2be](https://github.com/GnomeShift/TiSk/commit/395f2be7e0742ef8ab9ec0ec52ec02c784c66cef))
* **front:** change elements id to unique ([3945e92](https://github.com/GnomeShift/TiSk/commit/3945e92a88874ca031c0ccdd7d653ec72404f212))
* **front:** change ticket card style ([7a830bb](https://github.com/GnomeShift/TiSk/commit/7a830bbbac69d3dbf5201d96b8b9f6a1c863c5bf))
* **front:** change user menu style ([476a0e3](https://github.com/GnomeShift/TiSk/commit/476a0e3026b29a672e79edcdfc87637a3799f543))
* **front:** css styles optimization ([00691c2](https://github.com/GnomeShift/TiSk/commit/00691c21465c68180b4222bdd3c645eb0299f6c3))
* **front:** decrease ticket card size ([381f086](https://github.com/GnomeShift/TiSk/commit/381f086b90c21bff72fb2204fb62e098e7324e68))
* **front:** improve `match` rule ([f582064](https://github.com/GnomeShift/TiSk/commit/f582064dc6b768fcd638b8c7f0ee0b8556965a00))
* **front:** improve scripts in `package.json` ([73331fe](https://github.com/GnomeShift/TiSk/commit/73331fe323785e49503aa7e31a8b00f3531081f3))
* **front:** improve ticket assignment ([78f9750](https://github.com/GnomeShift/TiSk/commit/78f97502745d7b6e6f51359564910798d52a18e2))
* **front:** improve ticket assignment ([#17](https://github.com/GnomeShift/TiSk/issues/17)) ([0d9976f](https://github.com/GnomeShift/TiSk/commit/0d9976fa52aeefec6f3b96ef8c16ce1e338e9f52))
* **front:** improve user filter and search ([298589e](https://github.com/GnomeShift/TiSk/commit/298589e4978b209334b53645698e63e91e28f8d1))
* **front:** increase ticket form width ([4c0b503](https://github.com/GnomeShift/TiSk/commit/4c0b50389a6be6e59f0beba0a14136a2831aed3a))
* **front:** limit ticket title to 2 lines ([b24ccdb](https://github.com/GnomeShift/TiSk/commit/b24ccdbb6f9546c4d36774d058261bf3844dd953))
* **front:** move `getRoleLabel` to `utils.ts` ([78959a3](https://github.com/GnomeShift/TiSk/commit/78959a3b40a4a4c5f168cad6c4bf5e31775fdc7b))
* **front:** move `getStatusColor` and `getPriorityColor` to `utils.ts` ([3ce494d](https://github.com/GnomeShift/TiSk/commit/3ce494de9ad2ef152f74079c793c4ecca99ba05a))
* **front:** move `MyTickets.tsx` into `TicketList.tsx` ([b6071f0](https://github.com/GnomeShift/TiSk/commit/b6071f0e947b4f96b700ac0fbaf9e214fc6916f5))
* **front:** move `MyTickets.tsx` into `TicketList.tsx` ([#16](https://github.com/GnomeShift/TiSk/issues/16)) ([c14aedf](https://github.com/GnomeShift/TiSk/commit/c14aedf1d2a70a606a98a1fb6fafb583c58d8cf5))
* **front:** pages ([#27](https://github.com/GnomeShift/TiSk/issues/27)) ([a0ce4c0](https://github.com/GnomeShift/TiSk/commit/a0ce4c0aa896d6eb047ccc0536f1d1fba043aa68))
* **front:** remove ticket status change from details page ([229d0bc](https://github.com/GnomeShift/TiSk/commit/229d0bcc3259393e2a316dc1f29e2df9fca4098a))
* **front:** remove user status from profile ([b7c62f4](https://github.com/GnomeShift/TiSk/commit/b7c62f4b8817ee0c09574c6dfb32e44a80b7ea1b))
* **front:** rename `getPriorityColor` and `getStatusColor` to `getPriorityStyle` and `getStatusStyle` ([ad61450](https://github.com/GnomeShift/TiSk/commit/ad614503bea50617b7e7b8fe5ad1d80b723760d1))
* **front:** replace emoji with new ones ([a8aa13f](https://github.com/GnomeShift/TiSk/commit/a8aa13ff433a891c4b2235edf0ed79d56c81de1b))
* **front:** replace inline user editing with table ([b4c4f72](https://github.com/GnomeShift/TiSk/commit/b4c4f722dcfdc36a2d63c406852eff356aaaa3a0))
* **front:** set fixed priority size ([755d4ce](https://github.com/GnomeShift/TiSk/commit/755d4cec87b5db046dcb8265d599f4740786e8ab))
* **front:** styles optimization ([5596264](https://github.com/GnomeShift/TiSk/commit/559626427dfddfca7938e9ff89ecaf72bebe40c1))
* **front:** update `index.css` ([48055b2](https://github.com/GnomeShift/TiSk/commit/48055b28292ef43cfa246c257207a93865419dc8))
* **front:** update details page ([6f88069](https://github.com/GnomeShift/TiSk/commit/6f88069694539071060ccb9efefb22f6cba0abd6))
* **front:** validation rework ([ffa4a63](https://github.com/GnomeShift/TiSk/commit/ffa4a63dcc5f2ea2322f19b2d8e29cb9faf69464))
* improve validation ([9de5f06](https://github.com/GnomeShift/TiSk/commit/9de5f06d8be27b51dbcb8bf07ca0b2a008f04ba4))
* set default ticket status to `OPEN` ([5e80f93](https://github.com/GnomeShift/TiSk/commit/5e80f932f1fabd051d4868daad5e5fcb468dfb9e))
* set default ticket status to `OPEN` ([0a306a8](https://github.com/GnomeShift/TiSk/commit/0a306a817d49fee4fc6d9e2680bdd9f51f122414))
* ticket remains on user deletion ([007f67f](https://github.com/GnomeShift/TiSk/commit/007f67f5213fb28fc87d91f6f2257f6911c2bbab))


### 📚 Documentation

* add `README` ([80fd64d](https://github.com/GnomeShift/TiSk/commit/80fd64d8da08dd50627dc698c1f5f68296611377))
* add `README` ([#24](https://github.com/GnomeShift/TiSk/issues/24)) ([2f9100f](https://github.com/GnomeShift/TiSk/commit/2f9100f83948ee1826b2e1f64423a88cfb72eac3))
* update `README` ([6d71e91](https://github.com/GnomeShift/TiSk/commit/6d71e91d1783bd04fc06c878c513595c33c2f691))
* update `README` ([#34](https://github.com/GnomeShift/TiSk/issues/34)) ([a611144](https://github.com/GnomeShift/TiSk/commit/a611144ba7149ef8c4fcd2d1c22abeebe0b3d607))


### 🧪 Tests

* add tests ([#32](https://github.com/GnomeShift/TiSk/issues/32)) ([0fbcb6e](https://github.com/GnomeShift/TiSk/commit/0fbcb6ec063e5516b608c3b5dd4d638a99b3a02a))
* **back:** add tests ([a4c10c6](https://github.com/GnomeShift/TiSk/commit/a4c10c61121c16d52f6672cbb4e15afad06c847d))


### 👷 CI/CD

* add pipeline ([2ace896](https://github.com/GnomeShift/TiSk/commit/2ace89692df6f7179550d947e29fb505219da73e))
* add pipeline ([#35](https://github.com/GnomeShift/TiSk/issues/35)) ([98163a7](https://github.com/GnomeShift/TiSk/commit/98163a7e793f80d66349c384d6823233d1ee5111))


### 🧹 Chores

* bump version from `1.0.0` to `1.1.0` ([9996150](https://github.com/GnomeShift/TiSk/commit/999615038d0e029d6b2ac20a158a4b490f30368a))
* bump version to `1.11.9` ([6ccd3c8](https://github.com/GnomeShift/TiSk/commit/6ccd3c822b96893525bc7d6661698064eeedb71a))
* bump version to `1.12.6` ([db2ebdf](https://github.com/GnomeShift/TiSk/commit/db2ebdf2429bffb0138c940379b6d984c3f5f79a))
* bump version to `1.5.1` ([06399e7](https://github.com/GnomeShift/TiSk/commit/06399e786c6053fe3ca4257b64bbcf0671c74f86))
* bump version to `1.7.0` ([a537119](https://github.com/GnomeShift/TiSk/commit/a5371190abac8a9271d1a082410f1576831be6f7))
* bump version to `1.9.6` ([0aa3f62](https://github.com/GnomeShift/TiSk/commit/0aa3f6200dfd48e9d46481c07968de97a21e3bc7))
* **ci:** add deploy files ([db94459](https://github.com/GnomeShift/TiSk/commit/db94459a5a5a385530377d367416c3b2c90d2fd5))
* **ci:** add deploy files ([#20](https://github.com/GnomeShift/TiSk/issues/20)) ([8c60107](https://github.com/GnomeShift/TiSk/commit/8c6010766f15994d86d807bb9a320db4e5138b39))
* **ci:** update `Caddyfile` and `docker-compose.yml` ([85c9e25](https://github.com/GnomeShift/TiSk/commit/85c9e25eb532caf8a7e15e82bf66770a715fe4e6))
* **deps:** bump `js-yaml` to `4.1.1` ([164a786](https://github.com/GnomeShift/TiSk/commit/164a786159361bcaa8c5dede3eab57d60a529e0b))
* **deps:** bump `react-router` from `7.9.3` to `7.12.0` ([#31](https://github.com/GnomeShift/TiSk/issues/31)) ([933d251](https://github.com/GnomeShift/TiSk/commit/933d251f7b4131bd42b4a28e9821c307391285c2))
* **deps:** bump `vite` from `7.1.7` to `7.1.12` ([3178a18](https://github.com/GnomeShift/TiSk/commit/3178a18172034c17722158c4c75415a8b1f40aed))
* **deps:** bump `vite` from `7.1.7` to `7.1.12` ([#13](https://github.com/GnomeShift/TiSk/issues/13)) ([6a65902](https://github.com/GnomeShift/TiSk/commit/6a65902a9458ebdc575e7c85e4a429ef73bd2663))
* **deps:** bump react-router ([5812140](https://github.com/GnomeShift/TiSk/commit/5812140fc2acdb41bb1018d4918f8dac84ffdda5))
* release `1.12.6` ([#36](https://github.com/GnomeShift/TiSk/issues/36)) ([512e148](https://github.com/GnomeShift/TiSk/commit/512e1487735cbf52bca3aa2201af30f8412b581e))
* release `1.9.6-alpha` ([#25](https://github.com/GnomeShift/TiSk/issues/25)) ([fa5eeeb](https://github.com/GnomeShift/TiSk/commit/fa5eeebe6989b50f955d215859e1633cf37e846f))


### 📦 Build

* update build files ([f6c6a6c](https://github.com/GnomeShift/TiSk/commit/f6c6a6cb2b4137eb8a0084f37f8b591af0859442))
* update build files ([#33](https://github.com/GnomeShift/TiSk/issues/33)) ([c0adb85](https://github.com/GnomeShift/TiSk/commit/c0adb859e00c8b266736851d1c9663c91c606820))
