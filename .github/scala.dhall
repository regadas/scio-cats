let GithubActions =
      https://raw.githubusercontent.com/regadas/github-actions-dhall/master/package.dhall sha256:b42b062af139587666185c6fb72cc2994aa85a30065324174760b7d29a9d81c9

let matrix = toMap { scala = [ "2.12.11", "2.13.1" ] }

let publishEnv =
      toMap
        { PGP_PASSPHRASE = "\${{ secrets.PGP_PASSPHRASE }}"
        , SONATYPE_USERNAME = "\${{ secrets.SONATYPE_USERNAME }}"
        , SONATYPE_PASSWORD = "\${{ secrets.SONATYPE_PASSWORD }}"
        }

let setup =
      [ GithubActions.steps.checkout
      , GithubActions.steps.run
          { run =
              ''
              shasum build.sbt \
                project/plugins.sbt \
                project/build.properties \
                project/Dependencies.scala > gha.cache.tmp
              ''
          }
      , GithubActions.steps.cache
          { path = "~/.sbt", key = "sbt", hashFile = "gha.cache.tmp" }
      , GithubActions.steps.cache
          { path = "~/.cache/coursier"
          , key = "coursier"
          , hashFile = "gha.cache.tmp"
          }
      , GithubActions.steps.java-setup { java-version = "11" }
      ]

in  GithubActions.Workflow::{
    , name = "ci"
    , on = GithubActions.On::{
      , push = Some GithubActions.Push::{=}
      , pull_request = Some GithubActions.PullRequest::{=}
      }
    , jobs = toMap
        { checks = GithubActions.Job::{
          , name = "Checks"
          , runs-on = GithubActions.types.RunsOn.ubuntu-latest
          , steps =
                setup
              # [ GithubActions.steps.run
                    { run = "sbt scalafmtCheckAll scalafmtSbtCheck" }
                ]
          }
        , build = GithubActions.Job::{
          , name = "Build"
          , needs = Some [ "checks" ]
          , strategy = Some GithubActions.Strategy::{ matrix }
          , runs-on = GithubActions.types.RunsOn.ubuntu-latest
          , steps =
                setup
              # [ GithubActions.steps.run
                    { run = "sbt \"++\${{ matrix.scala}} test\"" }
                ,   GithubActions.steps.run
                      { run = ".github/scripts/gpg-setup.sh" }
                  ⫽ { env = Some
                        (toMap { GPG_SECRET = "\${{ secrets.GPG_SECRET }}" })
                    }
                ,   GithubActions.steps.run { run = "sbt +publishSigned" }
                  ⫽ { if = Some
                        "github.event_name == 'push' && github.ref == 'refs/heads/master'"
                    , env = Some publishEnv
                    }
                ,   GithubActions.steps.run
                      { run =
                          "sbt +publishSigned sonatypeBundleRelease ghpagesPushSite"
                      }
                  ⫽ { if = Some
                        "github.event_name == 'push' && startsWith(github.ref, 'refs/tags/v')"
                    , env = Some publishEnv
                    }
                ]
          }
        }
    }
